package com.github.sashjakk

import kotlin.time.TimedValue
import kotlin.time.measureTimedValue

fun main() {
    fun groupToRank(it: Map<Char, Int>): Int {
        val High = 1
        val OnePair = 2
        val TwoPair = 3
        val ThreeOfAKind = 4
        val FullHouse = 5
        val FourOfAKind = 6
        val FiveOfAKind = 7

        return when (it.size) {
            1 -> FiveOfAKind

            2 -> when {
                it.containsValue(4) -> FourOfAKind
                else -> FullHouse
            }

            3 -> when {
                it.containsValue(3) -> ThreeOfAKind
                else -> TwoPair
            }

            4 -> OnePair

            else -> High
        }
    }

    fun solve(
        input: List<String>,
        cards: Map<Char, Int>,
        group: (hand: String) -> Map<Char, Int>
    ): Int {
        val comparator = compareBy<String>(
            { group(it).let(::groupToRank) },
            { cards.getValue(it[0]) },
            { cards.getValue(it[1]) },
            { cards.getValue(it[2]) },
            { cards.getValue(it[3]) },
            { cards.getValue(it[4]) },
        )

        return input
            .map {
                val (hand, bid) = it.split(' ')
                hand to bid.toInt()
            }
            .sortedWith(compareBy(comparator) { it.first })
            .foldIndexed(0) { index, acc, (_, bid) -> acc + (bid * (index + 1)) }
    }

    fun part1(input: List<String>): TimedValue<Int> = measureTimedValue {
        val cards = "23456789TJQKA"
            .withIndex()
            .associate { (index, it) -> it to index }

        fun handToGroup(hand: String): Map<Char, Int> {
            return hand
                .groupingBy { it }
                .eachCount()
        }

        solve(input, cards, ::handToGroup)
    }

    fun part2(input: List<String>): TimedValue<Int> = measureTimedValue {
        val cards = "J23456789TQKA"
            .withIndex()
            .associate { (index, it) -> it to index }

        fun handToGroup(hand: String): Map<Char, Int> {
            val group = hand
                .groupingBy { it }
                .eachCount()
                .toMutableMap()

            if (
                (group.containsKey('J') && group.getValue('J') == 5) ||
                !group.containsKey('J')
            ) {
                return group
            }

            val jokers = group.remove('J')
                ?: return group

            val key = group
                .maxWith(
                    compareBy(
                        { it.value },
                        { cards.getValue(it.key) }
                    )
                )
                .key

            group[key] = (group[key] ?: 0) + jokers

            return group
        }

        solve(input, cards, ::handToGroup)
    }

    val input = readLines("Day07.txt")

    val (result1, time1) = part1(input)
    println("[Day 07 / 1 - $time1] - $result1")

    val (result2, time2) = part2(input)
    println("[Day 07 / 2 - $time2] - $result2")

}