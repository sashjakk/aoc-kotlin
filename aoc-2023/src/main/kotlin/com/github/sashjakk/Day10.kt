package com.github.sashjakk

import kotlin.math.absoluteValue
import kotlin.time.measureTimedValue

fun main() {
    val input = readLines("Day10.txt")

    data class Point<T>(val row: T, val column: T)

    data class Pipe(val type: Char, val position: Point<Int>) {
        val connections: List<Point<Int>>
            get() {
                val (r, c) = position

                return when (type) {
                    '|' -> listOf(Point(r - 1, c), Point(r + 1, c))
                    'L' -> listOf(Point(r - 1, c), Point(r, c + 1))
                    'J' -> listOf(Point(r - 1, c), Point(r, c - 1))
                    '-' -> listOf(Point(r, c - 1), Point(r, c + 1))
                    '7' -> listOf(Point(r, c - 1), Point(r + 1, c))
                    'F' -> listOf(Point(r, c + 1), Point(r + 1, c))
                    else -> emptyList()
                }
            }
    }

    fun pipe(input: List<String>, row: Int, column: Int): Pipe? =
        input
            .getOrNull(row)
            ?.getOrNull(column)
            ?.let { Pipe(it, Point(row, column)) }

    fun findStart(input: List<String>): Pipe {
        input.forEachIndexed { r, line ->
            line.forEachIndexed { c, symbol ->
                if (symbol == 'S') {
                    val point = Point(r, c)

                    val top = point in (pipe(input, r - 1, c)?.connections ?: emptyList())
                    val bottom = point in (pipe(input, r + 1, c)?.connections ?: emptyList())
                    val left = point in (pipe(input, r, c - 1)?.connections ?: emptyList())
                    val right = point in (pipe(input, r, c + 1)?.connections ?: emptyList())

                    val type = when {
                        top && bottom -> '|'
                        top && right -> 'L'
                        top && left -> 'J'
                        bottom && right -> 'F'
                        bottom && left -> '7'
                        left && right -> '-'
                        else -> throw NoSuchElementException("Unknown pipe type")
                    }

                    return Pipe(type, Point(r, c))
                }
            }
        }

        throw NoSuchElementException("Start not found")
    }

    tailrec fun walk(
        input: List<String>,
        current: Pipe,
        prev: Pipe,
        path: List<Pipe> = emptyList()
    ): List<Pipe> {
        return when {
            current.position == path.firstOrNull()?.position -> path
            else -> {
                val stage = current
                    .connections
                    .mapNotNull { (r, c) ->
                        input
                            .getOrNull(r)
                            ?.getOrNull(c)
                            ?.let { Pipe(it, Point(r, c)) }
                    }

                val next = stage
                    .first { it.position != prev.position }

                walk(input, next, current, path + current)
            }
        }
    }

    fun <T> solve(input: List<String>, block: (List<Pipe>) -> T) = measureTimedValue {
        val start = findStart(input)
        val (prev, current) = start.connections
            .asSequence()
            .mapNotNull {
                val next = pipe(input, it.row, it.column)
                    ?: return@mapNotNull null

                start to next
            }
            .first()

        val path = walk(input, current, prev, listOf(prev))

        block(path)
    }

    val (result1, time1) = solve(input) { it.size / 2 }
    println("[Day 10 / 1 - $time1] - $result1")

    val (result2, time2) = solve(input) { path ->
        // https://en.wikipedia.org/wiki/Shoelace_formula#Example
        val area = (path + path.first())
            .map { it.position }
            .zipWithNext { a, b -> a.row * b.column - a.column * b.row }
            .sum()
            .absoluteValue
            .let { it / 2 }

        // https://en.wikipedia.org/wiki/Pick%27s_theorem#Formula
        area - (path.size / 2) + 1
    }
    println("[Day 10 / 2 - $time2] - $result2")
}