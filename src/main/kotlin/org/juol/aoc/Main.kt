package org.juol.aoc

import org.juol.aoc.utils.*

private fun part1(input: String): Int = 0

private fun part2(input: String): Int = 0

fun main() {
    val testInput =
        """
        """.trimIndent()

    val testAnswer1 = 0
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 0
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
