import java.io.File

fun main() {
    val input = File("src/main/resources/Day04.txt")
        .readLines()

    data class Cell(val row: Int, val column: Int, val value: Int, var marked: Boolean = false)

    data class Board(val id: Int, val size: Int, val cells: MutableMap<Int, Cell>, var won: Boolean = false)

    fun isColumnFull(board: Board, columnIndex: Int): Boolean {
        val marked = board.cells
            .values
            .count { it.column == columnIndex && it.marked }

        return board.size == marked
    }

    fun isRowFull(board: Board, rowIndex: Int): Boolean {
        val marked = board.cells
            .values
            .count { it.row == rowIndex && it.marked }

        return board.size == marked
    }

    fun mark(board: Board, value: Int): Cell? {
        val cell = board.cells[value]
            ?: return null

        cell.marked = true
        return cell
    }

    fun calculateWinPoints(board: Board, multiplier: Int): Int {
        return board
            .cells
            .values
            .filter { !it.marked }
            .sumOf { it.value } * multiplier
    }

    val drawnNumbers = input
        .first()
        .split(',')
        .map { it.toInt() }

    val boards = input
        .asSequence()
        .drop(1)
        .filter { it.isNotEmpty() }
        .chunked(5).withIndex()
        .map { (id, rows) ->
            val cells = rows
                .flatMapIndexed { rowIndex, row ->
                    row
                        .trim()
                        .split("\\s+".toRegex())
                        .map { it.toInt() }
                        .mapIndexed { columnIndex, it -> it to Cell(rowIndex, columnIndex, it) }
                }
                .toMap(mutableMapOf())

            Board(id, 5, cells)
        }
        .toList()

    val boardScores = mutableListOf<Pair<Int, Int>>()

    for (drawn in drawnNumbers) {
        val active = boards.filterNot { it.won }
        if (active.isEmpty()) break

        for (board in active) {
            val cell = mark(board, drawn)
            if (cell === null) continue

            if (isColumnFull(board, cell.column) || isRowFull(board, cell.row)) {
                board.won = true
                val points = calculateWinPoints(board, drawn)
                boardScores += board.id to points
            }
        }
    }

    val firstWin = boardScores.first()
    val lastWin = boardScores.last()

    println("Day 04 / 1 - Board #${firstWin.first} - points: ${firstWin.second}")
    println("Day 04 / 2 - Board #${lastWin.first} - points: ${lastWin.second}")
}