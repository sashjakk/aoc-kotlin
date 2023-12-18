package com.github.sashjakk

import kotlin.math.absoluteValue
import kotlin.time.measureTimedValue

fun main() {
    val input = readLines("Day18.txt")

    fun solve(
        input: List<String>,
        parser: (value: String) -> Pair<Char, Long>
    ) = measureTimedValue {
        val parsed = input.map(parser)

        val points = parsed
            .fold(setOf(0L to 0L)) { acc, it ->
                val (x, y) = acc.last()

                val point = when (it.first) {
                    'D' -> x to y + it.second
                    'U' -> x to y - it.second
                    'L' -> x - it.second to y
                    'R' -> x + it.second to y
                    else -> throw NotImplementedError()
                }

                acc + point
            }

        val perimeter = parsed.sumOf { it.second }

        val area = (points + points.first())
            .zipWithNext { a, b -> a.first * b.second - a.second * b.first }
            .sumOf { it }
            .absoluteValue
            .let { it / 2 }

        area + perimeter / 2 + 1
    }

    val (result1, time1) = solve(input) {
        val (direction, meters, _) = it.split(" ")
        direction.first() to meters.toLong()
    }
    println("[Day 18 / 1 - $time1] - $result1")

    val (result2, time2) = solve(input) {
        val raw = it.split(" ").last()

        val meters = raw
            .drop(2)
            .take(5)
            .toInt(16)

        val direction = raw
            .dropLast(1)
            .last()
            .let {
                when (it) {
                    '0' -> 'R'
                    '1' -> 'D'
                    '2' -> 'L'
                    '3' -> 'U'
                    else -> throw NotImplementedError()
                }
            }

        direction to meters.toLong()
    }
    println("[Day 18 / 2 - $time2] - $result2")
}