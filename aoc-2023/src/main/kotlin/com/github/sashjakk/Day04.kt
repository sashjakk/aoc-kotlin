package com.github.sashjakk

import kotlin.math.pow

fun main() {
    data class Card(
        val winning: List<Int>,
        val numbers: List<Int>,
        var repeat: Int = 1
    )

    fun parseCards(input: List<String>): List<Card> {
        return input.map { line ->
            val (winning, numbers) = line
                .substringAfter(':')
                .split('|')
                .map {
                    it
                        .split(' ')
                        .map(String::trim)
                        .mapNotNull(String::toIntOrNull)
                }

            Card(winning, numbers)
        }
    }

    fun part1(input: List<String>): Int {
        return parseCards(input)
            .map { (a, b) -> b intersect a }
            .filter { it.isNotEmpty() }
            .sumOf { 2.0.pow(it.size - 1) }
            .toInt()
    }

    fun part2(input: List<String>): Int {
        val cards = parseCards(input)

        for (i in cards.indices) {
            val card = cards[i]
            val points = card.winning.intersect(card.numbers).size

            repeat(card.repeat) {
                for (j in i + 1..i + points) {
                    cards.getOrNull(j)?.also { it.repeat++ }
                }
            }
        }

        return cards.sumOf { it.repeat }
    }

    val input = readLines("Day04.txt")

    val result1 = part1(input)
    println("[Day 04 / 1] - $result1")

    val result2 = part2(input)
    println("[Day 04 / 2] - $result2")
}