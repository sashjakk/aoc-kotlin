package com.github.sashjakk

fun main() {
    val entries = readLines("Day05.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

typealias Stacks = MutableMap<Int, MutableList<Char>>

private fun parseStacks(entries: List<String>): Stacks =
    entries
        .reversed()
        .flatMap { line ->
            line
                .chunked(4)
                .map(String::trim)
                .mapIndexedNotNull { index, it ->
                    val value = it.getOrNull(1)
                        ?: return@mapIndexedNotNull null

                    index + 1 to value
                }
        }
        .groupByTo(
            destination = mutableMapOf(),
            keySelector = { it.first },
            valueTransform = { it.second },
        )

private data class Rearrangement(val amount: Int, val from: Int, val to: Int)

private fun parseRearrangement(input: String): Rearrangement {
    val parts = input.split(' ')

    return Rearrangement(
        amount = parts[1].toInt(),
        from = parts[3].toInt(),
        to = parts[5].toInt(),
    )
}

private fun <T> MutableList<T>.removeLast(times: Int): List<T> {
    val items = mutableListOf<T>()
    for (i in (size - 1) downTo (size - times)) {
        items.add(0, removeAt(i))
    }
    return items
}

private inline fun solve(
    entries: List<String>,
    block: (stack: Stacks, rearrangement: Rearrangement) -> Unit,
): String {
    val split = entries.indexOfFirst(String::isBlank)

    val stacks = parseStacks(entries.take(split - 1))

    for (i in (split + 1) until entries.size) {
        val line = entries[i]
        val rearrangement = parseRearrangement(line)

        block(stacks, rearrangement)
    }

    return stacks
        .values
        .map(List<Char>::last)
        .joinToString(separator = "")
}

private fun part1(entries: List<String>) = solve(entries) { stacks, (amount, from, to) ->
    val fromStack = stacks[from] ?: return@solve
    val toStack = stacks[to] ?: return@solve

    for (i in 0 until amount) {
        val item = fromStack.removeLastOrNull() ?: continue
        toStack += item
    }
}

private fun part2(entries: List<String>) = solve(entries) { stacks, (amount, from, to) ->
    val fromStack = stacks[from] ?: return@solve
    val toStack = stacks[to] ?: return@solve

    toStack += fromStack.removeLast(amount)
}
