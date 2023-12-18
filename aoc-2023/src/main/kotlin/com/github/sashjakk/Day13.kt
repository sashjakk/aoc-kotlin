package com.github.sashjakk

import kotlin.time.measureTimedValue

fun main() {
    val input = readLines("Day13.txt")

    tailrec fun group(
        input: List<String>,
        group: List<String> = emptyList(),
        collected: List<List<String>> = emptyList(),
    ): List<List<String>> {
        val line = input.firstOrNull()
            ?: return run {
                if (group.isNotEmpty()) collected + listOf(group)
                else collected
            }

        return if (line.isEmpty()) group(input.drop(1), emptyList(), collected + listOf(group))
        else group(input.drop(1), group + line, collected)
    }

    fun lines(input: List<String>): List<List<Char>> =
        input.map { it.toCharArray().toList() }

    fun columns(input: List<String>): List<List<Char>> {
        val columns = mutableListOf<MutableList<Char>>()
        var column = mutableListOf<Char>()

        for (c in input.first().indices) {
            for (r in input.indices) {
                column += input[r][c]
            }

            columns += column
            column = mutableListOf()
        }

        return columns
    }

    fun solve(
        input: List<String>,
        areElementEqual: (a: List<Char>, b: List<Char>) -> Boolean,
        isMirror: (left: List<List<Char>>, right: List<List<Char>>) -> Boolean,
    ) = measureTimedValue {
        val operations = listOf(
            Triple(::columns, isMirror) { idx: Int -> idx + 1 },
            Triple(::lines, isMirror) { idx: Int -> (idx + 1) * 100 }
        )

        group(input)
            .sumOf {
                operations
                    .firstNotNullOfOrNull { (prepare, areEqual, convert) ->
                        val data = prepare(it)

                        data
                            .windowed(2, 1)
                            .mapIndexedNotNull { index, (a, b) ->
                                if (areElementEqual(a, b)) index
                                else null
                            }
                            .firstOrNull { index ->
                                val left = data.take(index + 1).reversed()
                                val right = data.drop(index + 1)

                                areEqual(left, right)
                            }
                            ?.let(convert)
                    } ?: 0
            }
    }

    val (result1, time1) = solve(
        input = input,
        areElementEqual = { a, b -> a == b },
        isMirror = { left, right -> left.zip(right).all { (a, b) -> a == b } }
    )
    println("[Day 13 / 1 - $time1] - $result1")

    val (result2, time2) = solve(
        input = input,
        areElementEqual = { a, b -> a.zip(b).count { (x, y) -> x != y } <= 1 },
        isMirror = { left, right ->
            val delta = left
                .zip(right)
                .sumOf { (a, b) -> a.zip(b).count { (x, y) -> x != y } }

            delta == 1
        }
    )
    println("[Day 13 / 2 - $time2] - $result2")
}