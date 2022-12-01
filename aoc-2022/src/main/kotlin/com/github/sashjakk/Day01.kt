package com.github.sashjakk

fun main() {
    val entries = readLines("Day01.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

fun part1(entries: List<String>): Int {
    var max = Int.MIN_VALUE
    var sum = 0

    for (value in entries) {
        if (value.isNotBlank()) {
            sum += value.toInt()
            continue
        }

        if (sum > max) max = sum
        sum = 0
    }

    return max
}

fun part2(entries: List<String>): Int {
    return entries
        .bufferWhile { it.isNotBlank() }
        .map { it.sumOf(String::toInt) }
        .sortedDescending()
        .take(3)
        .sum()
}

fun <T> List<T>.bufferWhile(predicate: (T) -> Boolean): List<List<T>> {
    val groups = mutableListOf<List<T>>()
    var current = mutableListOf<T>()

    for (value in this) {
        if (predicate(value)) {
            current += value
            continue
        }

        groups.add(current)
        current = mutableListOf()
    }

    return groups
}