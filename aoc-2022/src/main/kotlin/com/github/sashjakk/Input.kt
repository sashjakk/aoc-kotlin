package com.github.sashjakk

fun readLines(path: String): List<String> {
    return object {}::class.java.getResourceAsStream("/${path}")
        ?.bufferedReader()
        ?.readLines()
        ?: emptyList()
}

fun readText(path: String): String {
    return object {}::class.java.getResourceAsStream("/${path}")
        ?.bufferedReader()
        ?.readText()
        ?: ""
}