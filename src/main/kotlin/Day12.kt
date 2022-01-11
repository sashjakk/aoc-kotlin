import java.io.File

fun main() {
    val input = File("src/main/resources/Day12.txt")
        .readLines()
        .map { it.split("-") }

    val segments = input
        .flatMap { (from, to) -> listOf(from to to, to to from) }
        .groupBy(keySelector = { it.first }, valueTransform = { it.second })

    fun String.isSmallCave() = first().isLowerCase()

    fun findPaths(
        point: String,
        path: List<String> = emptyList(),
        visited: Map<String, Boolean> = emptyMap(),
    ): List<List<String>> {
        if (point == "end") return listOf(path + point)

        return (segments[point] ?: emptyList())
            .filterNot {
                it.isSmallCave() && visited.getOrDefault(it, false)
            }
            .flatMap { findPaths(it, path + point, visited + (point to true)) }
    }

    fun findPaths2(point: String, path: List<String> = emptyList()): List<List<String>> {
        if (point == "end") {
            return listOf(path + point)
        }

        return (segments[point] ?: emptyList())
            .filter { cave ->
                when {
                    !cave.isSmallCave() -> true
                    cave !in path -> true
                    cave == "start" -> false
                    else -> path
                        .filter { it.isSmallCave() }
                        .groupBy { it }
                        .none { it.value.size == 2 }
                }
            }
            .flatMap { findPaths2(it, path + point) }
    }

//    val result1 = findPaths("start")
//    println("Day 12 / 1 - $result1")

    val result2 = findPaths2("start")
    for (i in result2) println(i)
    println(result2.size)
    println("Day 12 / 2 - ${result2.size}")
}