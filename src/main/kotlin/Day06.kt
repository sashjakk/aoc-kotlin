import java.io.File

fun main() {
    val input = File("src/main/resources/Day06.txt")
        .readText()
        .split(',')
        .map { it.toInt() }

    fun calculateFish(fish: MutableList<Int>, days: Int): Long {
        var population = buildMap<Int, Long> {
            for (f in fish) put(f, (get(f) ?: 0) + 1)
        }

        for (i in 0 until days) {
            population = population
                .flatMap { (key, value) ->
                    when (key) {
                        0 -> listOf(6 to value, 8 to value)
                        else -> listOf((key - 1) to value)
                    }
                }
                .groupBy(keySelector = { it.first }, valueTransform = { it.second })
                .map { (key, value) -> key to value.sum() }
                .toMap()
        }

        return population.values.sum()
    }

    val result1 = calculateFish(input.toMutableList(), 80)
    println("Day 06 / 1 - $result1")

    val result2 = calculateFish(input.toMutableList(), 256)
    println("Day 06 / 2 - $result2")
}