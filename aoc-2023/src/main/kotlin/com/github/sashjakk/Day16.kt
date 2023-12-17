package com.github.sashjakk

import combinations
import kotlin.time.measureTimedValue

fun main() {
    data class Point(val x: Int, val y: Int) {
        fun move(direction: Direction): Point {
            return when (direction) {
                Direction.UP -> Point(x, y - 1)
                Direction.DOWN -> Point(x, y + 1)
                Direction.LEFT -> Point(x - 1, y)
                Direction.RIGHT -> Point(x + 1, y)
            }
        }
    }

    fun reflect(
        input: List<String>,
        start: Pair<Direction, Point>
    ): MutableMap<Pair<Direction, Point>, Int> {
        val visited = mutableMapOf(start to 0)
        val queue = mutableListOf(start)

        while (true) {
            if (queue.isEmpty()) break

            val (direction, current) = queue.removeAt(0)

            val symbol = input
                .getOrNull(current.y)
                ?.getOrNull(current.x)
                ?: continue

            val times = visited.getOrDefault((direction to current), 0)
            visited += (direction to current) to (times + 1)

            val neighbours = when (symbol) {
                '.' -> listOf(
                    direction to when (direction) {
                        Direction.RIGHT -> current.copy(x = current.x + 1)
                        Direction.LEFT -> current.copy(x = current.x - 1)
                        Direction.UP -> current.copy(y = current.y - 1)
                        Direction.DOWN -> current.copy(y = current.y + 1)
                    }
                )

                '-' -> {
                    when (direction) {
                        Direction.RIGHT -> listOf(direction to current.copy(x = current.x + 1))
                        Direction.LEFT -> listOf(direction to current.copy(x = current.x - 1))
                        Direction.UP, Direction.DOWN -> {
                            listOf(
                                Direction.RIGHT to current.copy(x = current.x + 1),
                                Direction.LEFT to current.copy(x = current.x - 1),
                            )
                        }
                    }
                }

                '|' -> {
                    when (direction) {
                        Direction.RIGHT, Direction.LEFT -> {
                            listOf(
                                Direction.DOWN to current.copy(y = current.y + 1),
                                Direction.UP to current.copy(y = current.y - 1),
                            )
                        }

                        Direction.UP -> listOf(direction to current.copy(y = current.y - 1))
                        Direction.DOWN -> listOf(direction to current.copy(y = current.y + 1))
                    }
                }

                '/' -> {
                    when (direction) {
                        Direction.RIGHT -> listOf(Direction.UP to current.copy(y = current.y - 1))
                        Direction.LEFT -> listOf(Direction.DOWN to current.copy(y = current.y + 1))
                        Direction.UP -> listOf(Direction.RIGHT to current.copy(x = current.x + 1))
                        Direction.DOWN -> listOf(Direction.LEFT to current.copy(x = current.x - 1))
                    }
                }

                '\\' -> {
                    when (direction) {
                        Direction.RIGHT -> listOf(Direction.DOWN to current.copy(y = current.y + 1))
                        Direction.LEFT -> listOf(Direction.UP to current.copy(y = current.y - 1))
                        Direction.UP -> listOf(Direction.LEFT to current.copy(x = current.x - 1))
                        Direction.DOWN -> listOf(Direction.RIGHT to current.copy(x = current.x + 1))
                    }
                }

                else -> throw NotImplementedError()
            }

            queue += neighbours.filterNot { visited.containsKey(it) }
        }

        return visited
    }

    val input = readLines("Day16.txt")

    val (result1, time1) = measureTimedValue {
        reflect(input, Direction.RIGHT to Point(0, 0))
            .keys
            .map { it.second }
            .toSet()
            .size
    }
    println("[Day 16 / 1 - $time1] - $result1")

    val (result2, time2) = measureTimedValue {
        val down = combinations(elements = input.first().indices.toList(), length = 2)
            .filter { (_, y) -> y == 0 }
            .map { Direction.DOWN to Point(it[0], it[1]) }

        val right = combinations(elements = input.indices.toList(), length = 2)
            .filter { (x) -> x == 0 }
            .map { Direction.RIGHT to Point(it[0], it[1]) }

        val left = combinations(elements = input.indices.toList(), length = 2)
            .filter { (x) -> x == input.first().length - 1 }
            .map { Direction.LEFT to Point(it[0], it[1]) }

        val up = combinations(elements = input.first().indices.toList(), length = 2)
            .filter { (_, y) -> y == input.size - 1 }
            .map { Direction.UP to Point(it[0], it[1]) }

        (down + left + right + up)
            .maxOf {
                reflect(input, it)
                    .keys
                    .map { entry -> entry.second }
                    .toSet()
                    .size
            }
    }
    println("[Day 16 / 2 - $time2] - $result2")
}

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}