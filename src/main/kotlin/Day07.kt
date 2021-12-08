import java.io.File
import kotlin.math.abs

fun main() {
    val input = File("src/main/resources/Day07.txt")
        .readText()
        .split(',')
        .map { it.toInt() }

    fun List<Int>.calculateExpenses(price: (index: Int, value: Int) -> Int) =
        (0..maxOf { it }).minOf { position -> sumOf { price(position, it) } }

    val result1 = input.calculateExpenses { index, value -> abs(index - value) }
    println(result1)

    val result2 = input.calculateExpenses { index, value -> (abs(index - value) * (abs(index - value) + 1)) / 2 }
    println(result2)
}