package com.github.sashjakk

fun main() {
    fun Char.isSymbol(): Boolean = this != '.' && !isDigit()

    fun Pair<Int, Int>.adjacent(): List<Pair<Int, Int>> =
        buildList {
            for (i in first - 1..first + 1) {
                for (j in second - 1..second + 1) {
                    if (i == first && j == second) continue

                    add(i to j)
                }
            }
        }

    fun part1(input: List<String>): Int {
        var result1 = 0
        for (i in input.indices) {
            var number = 0
            var isValid = false

            for (j in 0..<input[i].length) {
                val current = input[i][j]

                if (current.isDigit()) {
                    number = number * 10 + current.digitToInt()

                    if (isValid) continue

                    isValid = (i to j)
                        .adjacent()
                        .mapNotNull { input.getOrNull(it.first)?.getOrNull(it.second) }
                        .any { it.isSymbol() }
                } else {
                    if (number != 0) println("$number is $isValid")

                    if (isValid) result1 += number

                    isValid = false
                    number = 0
                }
            }

            if (isValid) result1 += number
        }

        return result1
    }

    fun part2(input: List<String>): Int {
        val gears = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()

        for (i in input.indices) {
            var number = 0
            var key: Pair<Int, Int>? = null

            for (j in 0..<input[i].length) {
                val current = input[i][j]

                if (current.isDigit()) {
                    number = number * 10 + current.digitToInt()

                    if (key != null) continue


                    (i to j)
                        .adjacent()
                        .map { it to input.getOrNull(it.first)?.getOrNull(it.second) }
                        .firstOrNull { it.second == '*' }
                        ?.also { key = it.first }
                } else {
                    key
                        ?.let {
                            gears
                                .getOrPut(it) { mutableListOf() }
                                .also { it.add(number) }
                        }

                    key = null
                    number = 0
                }
            }

            key?.let {
                val group = gears.getOrPut(it) { mutableListOf() }
                group.add(number)
            }
        }

        return gears
            .filter { it.value.size == 2 }
            .map { it.value.reduce { a, b -> a * b } }
            .sum()
    }

    val input = readLines("Day03.txt")

    val result1 = part1(input)
    println("[Day 03 / 1] - $result1")

    val result2 = part2(input)
    println("[Day 03 / 2] - $result2")
}
