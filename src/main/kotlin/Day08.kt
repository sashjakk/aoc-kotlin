import java.io.File

fun main() {
    val input = File("src/main/resources/Day08.txt")
        .readLines()

    fun part1(input: List<String>) = input
        .map { it.split(" | ").last() }
        .flatMap { it.split(" ").map(String::length) }
        .sumOf {
            when (it) {
                2, 3, 4, 7 -> 1.toInt()
                else -> 0
            }
        }

    val result1 = part1(input)
    println("Day 08 / 1 - $result1")

    fun part2(input: List<String>): Int {
        fun <T> MutableList<T>.firstAndRemove(predicate: (T) -> Boolean): T = first(predicate).also { remove(it) }

        fun String.toBinaryString() = ('a'..'g').map { if (contains(it)) 1 else 0 }.joinToString("")
        fun List<Char>.toBinaryString() = joinToString("").toBinaryString()

        fun makeDecoder(patterns: List<List<Char>>): Map<String, Int> {
            val input = patterns.toMutableList()
            val remap = mutableMapOf<Int, List<Char>>()

            remap[1] = input.firstAndRemove { it.size == 2 }
            remap[7] = input.firstAndRemove { it.size == 3 }
            remap[4] = input.firstAndRemove { it.size == 4 }
            remap[8] = input.firstAndRemove { it.size == 7 }
            remap[6] = input.firstAndRemove { it.size == 6 && !it.containsAll(remap[1]!!) }
            remap[9] = input.firstAndRemove { it.size == 6 && it.containsAll(remap[4]!!) }
            remap[0] = input.firstAndRemove { it.size == 6 }
            remap[3] = input.firstAndRemove { it.size == 5 && it.containsAll(remap[1]!!) }
            remap[5] = input.firstAndRemove { it.size == 5 && it.containsAll(remap[9]!! - remap[3]!!) }
            remap[2] = input.last()

            return remap
                .map { (value, letters) -> letters.toBinaryString() to value }
                .toMap()
        }

        return input.sumOf { line ->
            val raw = line.split(" | ")
            val patterns = raw.first().split(" ").map(String::toList)
            val digits = raw.last().split(" ")

            val decoder = makeDecoder(patterns)

            digits
                .map { it.toBinaryString() }
                .map { decoder[it] }
                .joinToString("")
                .toInt()
        }
    }

    val result2 = part2(input)
    println("Day 08 / 2 - $result2")
}