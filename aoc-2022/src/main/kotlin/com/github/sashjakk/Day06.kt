package com.github.sashjakk

fun main() {
    val entries = readText("Day06.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

private fun solve(input: String, markerSize: Int) =
    markerSize + input
        .windowed(markerSize, 1)
        .indexOfFirst { it.toSet().size == markerSize }

private fun part1(input: String) = solve(input, 4)

private fun part2(input: String) = solve(input, 14)

