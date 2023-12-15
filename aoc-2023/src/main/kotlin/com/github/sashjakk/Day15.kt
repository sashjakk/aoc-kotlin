package com.github.sashjakk

import kotlin.time.measureTimedValue

fun main() {
    val input = readText("Day15.txt")

    fun hash(line: String): Int =
        line.fold(0) { acc, it -> (acc + it.code) * 17 % 256 }

    val (result1, time1) = measureTimedValue {
        input
            .split(",")
            .sumOf(::hash)
    }
    println("[Day 15 / 1 - $time1] - $result1")

    val (result2, time2) = measureTimedValue {
        val boxes = buildMap {
            repeat(256) {
                put(it, mutableMapOf<String, Int>())
            }
        }

        input
            .split(',')
            .forEach {
                if ('=' in it) {
                    val (box, focal) = it.split('=')
                    val boxId = hash(box)

                    boxes
                        .getValue(boxId)
                        .put(box, focal.toInt())

                } else {
                    val box = it.split('-').first()
                    val boxId = hash(box)

                    boxes
                        .getValue(boxId)
                        .remove(box)
                }
            }

        boxes
            .filter { it.value.isNotEmpty() }
            .map { (key, value) ->
                value
                    .values
                    .mapIndexed { index, focal -> (key + 1) * (index + 1) * focal }
                    .sum()
            }
            .sum()
    }
    println("[Day 15 / 2 - $time2] - $result2")
}