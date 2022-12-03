package com.github.sashjakk

fun main() {
    val entries = readLines("Day03.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

private val Char.priority
    get() = when {
        isLowerCase() -> code - ('a'.code - 1)
        isUpperCase() -> code - ('A'.code - 27)
        else -> throw IllegalArgumentException()
    }

private fun List<List<String>>.prioritySum(): Int {
    return this
        .map {
            it.reduce { a, b ->
                a.toSet()
                    .intersect(b.toSet())
                    .joinToString(separator = "")
            }
        }
        .sumOf { it.first().priority }
}

private fun part1(entries: List<String>): Int {
    return entries
        .map {
            listOf(
                it.substring(0 until it.length / 2),
                it.substring(it.length / 2 until it.length),
            )
        }
        .prioritySum()
}

private fun part2(entries: List<String>): Int {
    return entries
        .chunked(3)
        .prioritySum()
}