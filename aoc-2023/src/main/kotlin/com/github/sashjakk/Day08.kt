package com.github.sashjakk

import lcm
import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

fun main() {
    fun solve(
        input: List<String>,
        start: (value: String) -> Boolean,
        end: (value: String) -> Boolean,
    ): TimedValue<Long> = measureTimedValue {
        val regex = "([A-Za-z0-9]{3})".toRegex()

        val steps = input
            .first()
            .toCharArray()

        val network = input
            .asSequence()
            .filter { it.contains('=') }
            .associate { line ->
                val (node, left, right) = regex
                    .findAll(line)
                    .map { it.value }
                    .toList()

                node to (left to right)
            }

        var now = network.filter { start(it.key) }

        var index = 0
        var iterations = 0

        val paths = mutableListOf<Int>()

        while (now.isNotEmpty()) {
            now
                .filter { end(it.key) }
                .onEach { paths += iterations }

            val step = steps[index]

            now = now
                .filterNot { end(it.key) }
                .map {
                    when (step) {
                        'L' -> it.value.first to network.getValue(it.value.first)
                        'R' -> it.value.second to network.getValue(it.value.second)
                        else -> throw NotImplementedError()
                    }
                }
                .toMap()

            index = (index + 1) % steps.size
            iterations++
        }

        paths
            .map(Int::toLong)
            .reduce(::lcm)
    }

    val input = readLines("Day08.txt")

    val (result1, time1) = solve(
        input = input,
        start = { it == "AAA" },
        end = { it == "ZZZ" }
    )
    println("[Day 08 / 1 - $time1] - $result1")

    val (result2, time2) = solve(
        input = input,
        start = { it.endsWith('A') },
        end = { it.endsWith('Z') }
    )
    println("[Day 07 / 2 - $time2] - $result2")
}