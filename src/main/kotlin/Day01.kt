import java.io.File

fun main() {
    val input = File("src/main/resources/Day01.txt")
        .readLines()
        .map(String::toInt)

    val result1 = input
        .countIncrements()
    println("[Day 01 / 1] - $result1")

    val result2 = input
        .windowed(3, 1) { it.sum() }
        .countIncrements()
    println("[Day 01 / 2] - $result2")
}

fun List<Int>.countIncrements(): Int {
    val start = 0 to first()

    return this
        .drop(1)
        .fold(start) { (amount, prev), it ->
            val increments = if (it > prev) amount + 1 else amount
            increments to it
        }
        .first
}
