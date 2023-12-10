package com.github.sashjakk

import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

fun main() {
    tailrec fun predict(
        numbers: List<Int>,
        prediction: Int = 0,
    ): Int {
        return when {
            numbers.all { it == 0 } -> prediction
            else -> predict(
                numbers = numbers.windowed(2).map { (a, b) -> b - a },
                prediction = prediction + numbers.last()
            )
        }
    }

    fun solve(
        input: List<String>,
        reorder: (values: List<Int>) -> List<Int>
    ): TimedValue<Int> = measureTimedValue {
        input
            .map {
                it
                    .split(' ')
                    .mapNotNull(String::toIntOrNull)
            }
            .map(reorder)
            .sumOf(::predict)
    }

    val input = readLines("Day09.txt")

    val (result1, time1) = solve(input) { it }
    println("[Day 09 / 1 - $time1] - $result1")

    val (result2, time2) = solve(input) { it.reversed() }
    println("[Day 09 / 2 - $time2] - $result2")
}