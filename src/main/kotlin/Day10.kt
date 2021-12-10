import java.io.File

fun main() {
    val input = File("src/main/resources/Day10.txt")
        .readLines()

    fun Char.isOpen() = this in listOf('(', '{', '[', '<')
    fun Char.isClose() = this in listOf(')', '}', ']', '>')

    fun Char.convertToOpen() = when (this) {
        ')' -> '('
        ']' -> '['
        '>' -> '<'
        '}' -> '{'
        else -> throw NotImplementedError()
    }

    fun Char.convertToClose() = when (this) {
        '(' -> ')'
        '[' -> ']'
        '<' -> '>'
        '{' -> '}'
        else -> throw NotImplementedError()
    }

    val errors = mutableListOf<Char>()
    val incompleteLines = input.toMutableList()
    for (line in input) {
        val stack = mutableListOf<Char>()
        for (c in line) {
            when {
                c.isOpen() -> stack += c
                c.isClose() -> {
                    if (stack.last() == c.convertToOpen()) {
                        stack.removeLast()
                        continue
                    }

                    errors += c
                    incompleteLines.remove(line)
                    break
                }
            }
        }
    }

    val result1 = errors
        .onEach { println(it) }
        .sumOf {
            when (it) {
                ')' -> 3.toInt()
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> 0
            }
        }
    println("Day 10 / 1 - $result1")

    val completions = incompleteLines
        .map { line ->
            val stack = mutableListOf<Char>()
            for (c in line) {
                when {
                    c.isOpen() -> stack.add(c)
                    c.isClose() -> if (stack.last() == c.convertToOpen()) {
                        stack.removeLast()
                    }
                }
            }

            stack.map { it.convertToClose() }.reversed()
        }

    val scores = completions
        .map { line ->
            line.fold(0L) { acc, it ->
                val points = when (it) {
                    ')' -> 1
                    ']' -> 2
                    '}' -> 3
                    '>' -> 4
                    else -> 0
                }

                acc * 5L + points
            }
        }

    val result2 = scores.sorted()[scores.size / 2]
    println("Day 10 / 2 - $result2")
}