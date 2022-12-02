package com.github.sashjakk

import kotlin.math.abs

fun main() {
    val entries = readLines("Day02.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

sealed class Move(val points: Int)
object Rock : Move(1)
object Paper : Move(2)
object Scissors : Move(3)

private fun parseMove(move: String): Move? = when (move) {
    "A", "X" -> Rock
    "B", "Y" -> Paper
    "C", "Z" -> Scissors
    else -> null
}

sealed class Result(val points: Int)
object Win : Result(6)
object Draw : Result(3)
object Lose : Result(0)

private fun parseResult(result: String): Result? = when (result) {
    "X" -> Lose
    "Y" -> Draw
    "Z" -> Win
    else -> null
}

private fun Move.score(opponent: Move): Result = when {
    points == opponent.points -> Draw
    abs(points - opponent.points) == 1 -> if (points > opponent.points) Win else Lose
    abs(points - opponent.points) == 2 -> if (this is Rock) Win else Lose
    else -> throw IllegalStateException()
}

private fun part1(entries: List<String>): Int {
    return entries
        .map { raw -> raw.split(" ").mapNotNull(::parseMove) }
        .sumOf { (elf, me) -> me.points + me.score(elf).points }
}

private fun part2(entries: List<String>): Int {
    return entries
        .mapNotNull {
            val raw = it.split(" ")
            val move = parseMove(raw[0]) ?: run { return@mapNotNull null }
            val result = parseResult(raw[1]) ?: run { return@mapNotNull null }

            move to result
        }
        .sumOf { (move, result) ->
            result.points + when (result) {
                Draw -> move.points
                Lose -> if (move.points - 1 <= 0) 3 else move.points - 1
                Win -> if (move.points + 1 > 3) 1 else move.points + 1
            }
        }
}