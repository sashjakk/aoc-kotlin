package com.github.sashjakk

fun main() {
    val entries = readLines("Day07.txt")

    val result1 = part1(entries)
    println("1st - $result1")

    val result2 = part2(entries)
    println("2nd - $result2")
}

private sealed interface FileSystem {
    val size: Int
}

private data class File(val name: String, override val size: Int) : FileSystem

private class Dir(val name: String, val content: MutableList<FileSystem> = mutableListOf()) : FileSystem {
    override val size: Int
        get() = content.fold(0) { acc, it -> acc + it.size }
}

private fun String.lastWord() = takeLastWhile { !it.isWhitespace() }

private fun makePath(vararg segment: String?) =
    segment
        .filterNot { it.isNullOrBlank() }
        .joinToString("/")

private fun truncatePath(path: String) =
    path
        .split("/")
        .dropLast(1)
        .joinToString("/")

private fun parseFileSystem(entries: List<String>): Map<String, Dir> {
    val fileSystem = mutableMapOf<String, Dir>()
    var cwd: Dir? = null

    for (line in entries) {
        when {
            line.startsWith("$ cd ") -> {
                val path = when (val next = line.lastWord()) {
                    ".." -> truncatePath(cwd?.name ?: "")
                    else -> makePath(cwd?.name, next)
                }

                fileSystem.putIfAbsent(path, Dir(path))
                cwd = fileSystem[path]
            }

            line.startsWith("dir ") -> {
                val path = makePath(cwd?.name, line.lastWord())
                val dir = Dir(path)

                fileSystem.putIfAbsent(path, dir)

                cwd?.content?.add(dir)
            }

            line.first().isDigit() -> {
                val (size, next) = line.split(' ')
                val path = makePath(cwd?.name, next)

                cwd?.content?.add(File(path, size.toInt()))
            }
        }
    }

    return fileSystem
}

private fun part1(entries: List<String>) =
    parseFileSystem(entries)
        .values
        .fold(0) { acc, it ->
            val size = if (it.size <= 100000) it.size else 0
            acc + size
        }

private fun part2(entries: List<String>): Int {
    val filesystem = parseFileSystem(entries)
        .map { it.value.size }
        .sorted()

    val required = 30000000
    val total = 70000000

    val used = filesystem.last()
    val more = required - (total - used)

    return filesystem.first { it >= more }
}
