package com.github.sashjakk

fun main() {
    val input = readLines("Day09.txt")
    val rows = input.size
    val columns = input.first().length

    val minPoints = mutableListOf<Int>()
    for (r in 0 until rows) {
        for (c in 0 until columns) {
            val center = input[r][c].digitToInt()
            val neighbours = listOfNotNull(
                input.getOrNull(r)?.getOrNull(c - 1)?.digitToInt(),
                input.getOrNull(r)?.getOrNull(c + 1)?.digitToInt(),
                input.getOrNull(r - 1)?.getOrNull(c)?.digitToInt(),
                input.getOrNull(r + 1)?.getOrNull(c)?.digitToInt(),
            )

            if (neighbours.all { it > center }) {
                minPoints += center
            }
        }
    }

    val result1 = minPoints.sumOf { it + 1 }
    println("Day 09 / 1 - $result1")
}