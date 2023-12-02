package com.github.sashjakk

fun main() {
    val games = readLines("Day02.txt")
        .map { parseGame(it) }

    val configuration = mapOf("red" to 12, "green" to 13, "blue" to 14)

    val result1 = games
        .filter { isValid(configuration, it.gameSets) }
        .sumOf { it.id }

    println("[Day 02 / 1] - $result1")

    val result2 = games
        .map { findConfiguration(it.gameSets) }
        .sumOf { it.values.reduce(Int::times) }

    println("[Day 02 / 2] - $result2")
}

data class Game(
    val id: Int,
    val gameSets: List<Map<String, Int>>
)

fun parseGame(input: String): Game {
    val (part1, part2) = input.split(':')

    val id = part1.split(' ').last().toInt()
    val gameSets = part2
        .split(';')
        .map(::parseGameSet)

    return Game(id, gameSets)
}

fun parseGameSet(input: String): Map<String, Int> = input
    .split(',')
    .associate {
        val (value, key) = it.trim().split(' ')
        key to value.toInt()
    }

fun isValid(
    configuration: Map<String, Int>,
    input: List<Map<String, Int>>,
): Boolean {
    return input.all {
        for ((key, value) in it) {
            if (value > configuration.getValue(key)) return@all false
        }
        return@all true
    }
}

fun findConfiguration(input: List<Map<String, Int>>): Map<String, Int> {
    val initial = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)

    return input
        .fold(initial) { configuration, current ->
            for ((key, value) in current) {
                if (value > configuration.getValue(key)) configuration[key] = value
            }

            configuration
        }
        .toMap()
}