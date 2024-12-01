package org.juol.aoc

fun main() {
    fun part1(input: List<String>): Int = input.size

    fun part2(input: List<String>): Int = input.size

    val testAnswer1 = 0
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 0
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

val testInput =
    """
""".lines()
