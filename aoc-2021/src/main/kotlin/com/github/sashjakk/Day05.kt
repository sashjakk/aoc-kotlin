package com.github.sashjakk

fun main() {
    val input = readLines("Day05.txt")
        .map { raw ->
            raw
                .split(" -> ")
                .flatMap { it.split(',').map(String::toInt) }
        }

    fun makeDiagram(
        input: List<List<Int>>,
        supportedLineTypes: Map<String, Boolean>
    ): MutableMap<Pair<Int, Int>, Int> {
        val diagram = mutableMapOf<Pair<Int, Int>, Int>()

        fun mark(x: Int, y: Int) {
            val key = x to y
            diagram[key] = diagram.getOrDefault(key, 0) + 1
        }

        for ((x1, y1, x2, y2) in input) {
            val type = when {
                x1 == x2 -> "horizontal"
                y1 == y2 -> "vertical"
                else -> "diagonal"
            }

            if (!supportedLineTypes.getOrDefault(type, false)) continue

            if (type == "horizontal") {
                val range = if (y2 > y1) y1.rangeTo(y2) else y1.downTo(y2)
                for (y in range) mark(x1, y)
            }

            if (type == "vertical") {
                val range = if (x2 > x1) x1.rangeTo(x2) else x1.downTo(x2)
                for (x in range) mark(x, y1)
            }

            if (type == "diagonal") {
                val h = (if (x2 > x1) x1.rangeTo(x2) else x1.downTo(x2)).toList()
                val v = (if (y2 > y1) y1.rangeTo(y2) else y1.downTo(y2)).toList()
                for (i in h.indices) mark (h[i], v[i])
            }
        }

        return diagram
    }

    val diagram1 = makeDiagram(input, mapOf("horizontal" to true, "vertical" to true))
    val result1 = diagram1.values.count { it >= 2 }
    println("Day 05 / 1 - $result1")

    val diagram2 = makeDiagram(input, mapOf("horizontal" to true, "vertical" to true, "diagonal" to true))
    val result2 = diagram2.values.count { it >= 2 }
    println("Day 05 / 2 - $result2")
}