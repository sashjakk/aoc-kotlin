package com.github.sashjakk

fun main() {
    val input = readLines("Day01.txt")

    val result1 = input
        .sumOf {
            val first = it.first(Char::isDigit).digitToInt()
            val last = it.last(Char::isDigit).digitToInt()
            first * 10 + last
        }

    println("[Day 01 / 1] - $result1")

    val result2 = input
        .sumOf { line ->
            val first = line.findAnyOf(numbers.keys)?.let { numbers[it.second] } ?: 0
            val last = line.findLastAnyOf(numbers.keys)?.let { numbers[it.second] } ?: 0
            first * 10 + last
        }

    println("[Day 01 / 2] - $result2")
}

val numbers = mapOf(
    "one" to 1, "two" to 2, "three" to 3, "four" to 4, "five" to 5, "six" to 6, "seven" to 7, "eight" to 8, "nine" to 9,
    "0" to 0, "1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5, "6" to 6, "7" to 7, "8" to 8, "9" to 9
)