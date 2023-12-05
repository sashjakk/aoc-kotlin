package com.github.sashjakk


fun main() {
    fun parseSeedsAndMaps(input: List<String>): Pair<List<Long>, List<List<List<Long>>>> {
        val seeds = mutableListOf<Long>()
        val mappings = mutableListOf<List<List<Long>>>()

        var currentStage: MutableList<List<Long>>? = null

        for (line in input) {
            if (line.startsWith("seeds")) {
                line
                    .substringAfter(":")
                    .split(' ')
                    .mapNotNull(String::toLongOrNull)
                    .toCollection(seeds)

                continue
            }

            if (line.endsWith("map:")) {
                currentStage = mutableListOf()
                continue
            }

            if (currentStage != null && line.isNotEmpty()) {
                currentStage += line
                    .split(' ')
                    .mapNotNull(String::toLongOrNull)

                continue
            }

            if (currentStage != null && line == "") {
                mappings += currentStage
                currentStage = null
                continue
            }
        }

        currentStage?.let { mappings += it }

        return seeds.toList() to mappings
    }

    fun proceed(seed: List<Long>, mappings: List<List<Long>>): List<List<Long>> {
        val seeds = mutableListOf<List<Long>>()

        var current = seed

        while (true) {
            val shifter = mappings
                .firstOrNull { current[0] in it[1]..<it[1] + it[2] }

            if (shifter == null) {
                seeds += current
                break
            }

            val (dest, src, len) = shifter

            val start = current[0]
            val end = current[0] + current[1]

            val seedEnd = minOf(end, src + len)

            seeds += listOf(dest + (start - src), seedEnd - start)

            val leftover = end - seedEnd
            if (end - seedEnd > 0) {
                current = listOf(seedEnd, leftover)
                continue
            }

            break
        }

        return seeds
    }

    fun part1(input: List<String>): Long {
        val (seeds, mappings) = parseSeedsAndMaps(input)
        val initial = seeds.map { listOf(it, 1) }

        return mappings
            .fold(initial) { seed, map ->
                seed.flatMap { proceed(it, map) }
            }
            .minOf { it[0] }
    }

    fun part2(input: List<String>): Long {
        val (seeds, mappings) = parseSeedsAndMaps(input)
        val initial = seeds.chunked(2)

        return mappings
            .fold(initial) { seed, map ->
                seed.flatMap { proceed(it, map) }
            }
            .minOf { it[0] }
    }

    val input = readLines("Day05.txt")

    val result1 = part1(input)
    println("[Day 05 / 1] - $result1")

    val result2 = part2(input)
    println("[Day 05 / 2] - $result2")

}
