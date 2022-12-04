package com.github.sashjakk

fun main() {
    val entries = readLines("Day04.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

private fun parseSectionPairs(input: String): List<Pair<Int, Int>> {
    val (a, b) = input.split(',')

    val (from1, to1) = a.split('-').map(String::toInt)
    val (from2, to2) = b.split('-').map(String::toInt)

    return listOf(from1 to to1, from2 to to2)
}

private fun Pair<Int, Int>.contains(other: Pair<Int, Int>) =
    first >= other.first && second <= other.second

private fun Pair<Int, Int>.overlap(other: Pair<Int, Int>) =
    first in other.first .. other.second

private fun part1(entries: List<String>): Int {
    return entries
        .map(::parseSectionPairs)
        .count { (a, b) -> a.contains(b) or b.contains(a) }
}

private fun part2(entries: List<String>): Int {
    return entries
        .map(::parseSectionPairs)
        .count { (a, b) -> a.overlap(b) or b.overlap(a) }
}