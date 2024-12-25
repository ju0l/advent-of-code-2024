package org.juol.aoc

import org.juol.aoc.utils.*

private fun part1(input: String): Int = 0

private fun part2(input: String): Int = 0

fun main() {
    val testInput =
        """
        """.trimIndent()

    val testAnswer1 = 0
    val testResult1 = part1(testInput)
    check(testResult1 == testAnswer1) { "Answer 1 to test is wrong: $testAnswer1" }
    val testAnswer2 = 0
    val testResult2 = part2(testInput)
    check(testResult2 == testAnswer2) { "Answer 2 to test is wrong: $testAnswer2" }

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
