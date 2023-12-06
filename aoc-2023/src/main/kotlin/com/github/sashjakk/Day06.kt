package com.github.sashjakk

fun main() {
    fun part1(input: List<String>): Int {
        val (time, distance) = input
            .map { it.substringAfter(':') }
            .map {
                it
                    .split(' ')
                    .map(String::trim)
                    .mapNotNull(String::toIntOrNull)
            }

        return time
            .withIndex()
            .fold(1) { acc, (index, limit) ->
                acc * (0..limit)
                    .map { (limit - it) * it }
                    .count { it > distance[index] }
            }
    }

    fun part2(input: List<String>): Int {
        val (time, distance) = input
            .map { it.substringAfter(':') }
            .mapNotNull {
                it
                    .replace(" ", "")
                    .toLongOrNull()
            }

        return (0..time)
            .map { (time - it) * it }
            .count { it > distance }
    }

    val input = readLines("Day06.txt")

    val result1 = part1(input)
    println("[Day 06 / 1] - $result1")

    val result2 = part2(input)
    println("[Day 06 / 2] - $result2")
}