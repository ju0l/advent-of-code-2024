package org.juol.aoc.day02

import org.juol.aoc.utils.*
import kotlin.math.abs

private fun isRowSafe(row: List<Int>): Boolean {
    var inc = 0
    var desc = 0
    row.zipWithNext().forEach { (a, b) ->
        if (a == b || abs(a - b) > 3) return false
        if (a < b) {
            if (desc != 0) return false
            inc++
        } else {
            if (inc != 0) return false
            desc++
        }
    }
    return true
}

private fun part1(input: String): Int {
    val grid = input.toIntGrid()
    val safe = grid.rows().count { isRowSafe(it) }
    return safe
}

private fun part2(input: String): Int {
    val grid = input.toIntGrid()
    val safe =
        grid.rows().count { row ->
            if (isRowSafe(row)) return@count true

            for (i in row.indices) {
                val r = row.toMutableList().also { it.removeAt(i) }
                if (isRowSafe(r)) return@count true
            }

            false
        }

    return safe
}

fun main() {
    val testInput =
        """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
        """.trimIndent()

    val testAnswer1 = 2
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 4
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day02")
    // 390
    part1(input).println()
    // 439
    part2(input).println()
}
