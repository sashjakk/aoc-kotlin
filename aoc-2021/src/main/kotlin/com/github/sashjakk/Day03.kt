package com.github.sashjakk

fun main() {
    val input = readLines("Day03.txt")
        .map { it.map(Char::digitToInt) }

    fun part1(input: List<List<Int>>): Pair<Int, Int> {
        val lines = input.size
        val length = input.first().size

        val mostCommonBits = mutableListOf<Int>()
        val leastCommonBits = mutableListOf<Int>()

        for (c in 0 until length) {
            var sum = 0; for (r in 0 until lines) sum += input[r][c]
            mostCommonBits.add(c, if (sum < lines / 2) 0 else 1)
            leastCommonBits.add(c, if (sum > lines / 2) 0 else 1)
        }

        val gamma = mostCommonBits.joinToString(separator = "").toInt(2)
        val epsilon = leastCommonBits.joinToString(separator = "").toInt(2)

        return gamma to epsilon
    }

    val (gamma, epsilon) = part1(input)
    println("[Day 03 / 1] - $gamma $epsilon ${gamma * epsilon}")

    fun part2(input: List<List<Int>>): Pair<Int, Int> {
        val leastCommonBit = { zeros: Int, ones: Int ->
            if (zeros == ones) 0 else
            if (zeros < ones) 0 else 1
        }

        val mostCommonBit = { zeros: Int, ones: Int ->
            if (zeros == ones) 1 else
            if (zeros > ones) 0 else 1
        }

        fun filterByCommonBit(
            selectBit: (Int, Int) -> Int,
            input: List<List<Int>>,
            index: Int = 0,
        ): Int {
            val lines = input.size

            var zeros = 0
            var ones = 0

            for (r in 0 until lines) {
                when (input[r][index]) {
                    0 -> zeros += 1
                    1 -> ones += 1
                }
            }

            val selectedBit = selectBit(zeros, ones)

            val subset = mutableListOf<List<Int>>()
            for (r in 0 until lines) {
                if (input[r][index] == selectedBit) subset += input[r]
            }

            if (subset.size > 1) {
                return filterByCommonBit(selectBit, subset, index + 1)
            }

            return subset.first().joinToString(separator = "").toInt(2)
        }

        val oxygen = filterByCommonBit(mostCommonBit, input)
        val co2 = filterByCommonBit(leastCommonBit, input)

        return oxygen to co2
    }

    val (oxygen, co2) = part2(input)
    println("[Day 03 / 2] - $oxygen $co2 ${oxygen * co2}")
}