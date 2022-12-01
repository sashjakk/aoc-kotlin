package com.github.sashjakk

fun main() {
    val input = readLines("Day02.txt")
        .map {
            val (direction, value) = it.split(' ')
            direction to value.toInt()
        }

    fun part1(input: List<Pair<String, Int>>): Int {
        var horizontal = 0
        var vertical = 0

        for ((direction, value) in input) {
            when (direction) {
                "forward" -> horizontal += value
                "up" -> vertical -= value
                "down" -> vertical += value
            }
        }

        return horizontal * vertical
    }

    val result1 = part1(input)
    println("[Day 02 / 1] - $result1")

    fun part2(input: List<Pair<String, Int>>): Int {
        var aim = 0
        var horizontal = 0
        var vertical = 0

        for ((direction, value) in input) {
            when (direction) {
                "forward" -> {
                    horizontal += value
                    vertical += aim * value
                }
                "up" -> aim -= value
                "down" -> aim += value
            }
        }

        return horizontal * vertical
    }

    val result2 = part2(input)
    println("[Day 02 / 2] - $result2")
}