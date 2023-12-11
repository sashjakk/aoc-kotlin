package com.github.sashjakk

import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {
    val input = readLines("Day11.txt")

    fun galaxies(input: List<String>): List<Pair<Int, Int>> {
        val result = mutableListOf<Pair<Int, Int>>()

        input.forEachIndexed { row, line ->
            line.forEachIndexed { column, symbol ->
                if (symbol == '#') result += row to column
            }
        }

        return result.toList()
    }

    fun distance(a: Pair<Long, Long>, b: Pair<Long, Long>): Long =
        abs(a.first - b.first) + abs(a.second - b.second)

    fun combinations(a: IntRange, b: IntRange): Sequence<Pair<Int, Int>> =
        sequence {
            for (i in a.first..a.last) {
                for (j in i + 1..b.last) {
                    yield(i to j)
                }
            }
        }

    fun explode(coordinate: Int, free: Set<Int>, multiplier: Long): Long =
        coordinate.toLong() + free.count { coordinate > it } * multiplier

    fun solve(
        input: List<String>,
        multiplier: Long
    ) = measureTimedValue {
        val galaxies = galaxies(input)
        val rows = input.indices.toSet() - galaxies.map { it.first }.toSet()
        val columns = input.first().indices.toSet() - galaxies.map { it.second }.toSet()

        val times = multiplier - 1

        combinations(galaxies.indices, galaxies.indices)
            .mapNotNull { pair ->
                val from = galaxies.getOrNull(pair.first)
                    ?.let { (x, y) ->
                        explode(x, rows, times) to explode(y, columns, times)
                    }
                    ?: return@mapNotNull null

                val to = galaxies.getOrNull(pair.second)
                    ?.let { (x, y) ->
                        explode(x, rows, times) to explode(y, columns, times)
                    }
                    ?: return@mapNotNull null

                from to to
            }
            .sumOf { distance(it.first, it.second) }
    }

    val (result1, time1) = solve(input, 2)
    println("[Day 11 / 1 - $time1] - $result1")

    val (result2, time2) = solve(input, 1_000_000)
    println("[Day 11 / 2 - $time2] - $result2")
}