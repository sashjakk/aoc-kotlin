package com.github.sashjakk

fun main() {
    val input = readLines("Day11.txt")
        .map { it.mapTo(mutableListOf(), Char::digitToInt) }

    val rows = input.size
    val columns = input.first().size

    fun adjacent(x: Int, y: Int): List<Pair<Int, Int>> {
        return listOf(
            x - 1 to y + 1,
            x - 1 to y,
            x - 1 to y - 1,
            x to y - 1,
            x to y + 1,
            x + 1 to y + 1,
            x + 1 to y,
            x + 1 to y - 1,
        )
    }

    fun <T> List<MutableList<T>>.writeIfPossible(x: Int, y: Int, operation: (value: T) -> T): T? {
        if (x in indices && y in this[x].indices) {
            val value = this[x][y]
            val newValue = operation(value)
            this[x][y] = newValue
            return newValue
        }

        return null
    }

    fun <T> List<MutableList<T>>.getAtOrNull(x: Int, y: Int): T? =
        if (x in indices && y in this[x].indices) this[x][y] else null

    fun <T> List<MutableList<T>>.display() {
        for (line in this) {
            println(line.joinToString(""))
        }
    }

    fun List<MutableList<Int>>.flash(x: Int, y: Int): Int {
        val value = getAtOrNull(x, y)
        if (value == null || value == 0 || value <= 9) return 0

        var flashed = 1
        writeIfPossible(x, y) { 0 }

        for (neighbour in adjacent(x, y)) {
            writeIfPossible(neighbour.first, neighbour.second) { if (it == 0) 0 else it + 1 }
            flashed += flash(neighbour.first, neighbour.second)
        }

        return flashed
    }

    fun part1(): Int {
        val max = 195
        var iteration = 0
        var flashes = 0
        while (iteration < max) {
            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    input[r][c] += 1
                }
            }

            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    flashes += input.flash(r, c)
                }
            }

            iteration++
        }

        return flashes
    }

    val result1 = part1()
    println("Day 11 / 1 - $result1")

    fun part2(): Int {
        var step = 0
        while (true) {
            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    input[r][c] += 1
                }
            }

            for (r in 0 until rows) {
                for (c in 0 until columns) {
                    input.flash(r, c)
                }
            }

            step++

            if (input.sumOf { it.sum() } == 0) {
                println("step $step")
                break
            }
        }
        return step
    }

    val result2 = part2()
    println("Day 11 / 2 - $result2")
}