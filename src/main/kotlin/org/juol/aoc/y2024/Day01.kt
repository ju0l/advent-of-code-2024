package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.abs

private fun part1(input: String): Int {
    val (left, right) = input.toIntGrid().columns().map { it.sorted() }
    val sum = left.zip(right).sumOf { (l, r) -> abs(l - r) }
    return sum
}

private fun part2(input: String): Int {
    val (left, right) = input.toIntGrid().columns()
    val sum = left.sumOf { l -> right.sumOf { r -> if (l == r) l else 0 } }
    return sum
}

fun main() {
    val testInput =
        """
        3   4
        4   3
        2   5   
        1   3
        3   9
        3   3
        """.trimIndent()

    val testAnswer1 = 11
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 31
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day01")
    // 2192892
    part1(input).println()
    // 22962826
    part2(input).println()
}
