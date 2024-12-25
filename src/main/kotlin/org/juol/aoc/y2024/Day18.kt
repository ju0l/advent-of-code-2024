package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private fun parse(input: String) =
    input
        .lines()
        .map { row -> row.split(",").let { it[0].toInt() to it[1].toInt() } }

private fun part1(
    input: String,
    bytesCount: Int,
    gridSize: Int,
): Int {
    val grid = filledGrid(gridSize, gridSize, ".")
    parse(input)
        .take(bytesCount)
        .forEach { p -> grid[p] = "#" }

    val dst = grid.shortestDst(0 to 0, gridSize - 1 to gridSize - 1, "#")
    return dst
}

private fun part2(
    input: String,
    gridSize: Int,
): String {
    val grid = filledGrid(gridSize, gridSize, ".")

    val bytes = parse(input)

    for (b in bytes) {
        grid[b] = "#"
        val s = grid.shortestDst(0 to 0, gridSize - 1 to gridSize - 1, "#")
        if (s == Int.MAX_VALUE) {
            return "${b.x},${b.y}"
        }
    }

    return ""
}

fun main() {
    val testInput =
        """
        5,4
        4,2
        4,5
        3,0
        2,1
        6,3
        2,4
        1,5
        0,6
        3,3
        2,6
        5,1
        1,2
        5,5
        2,5
        6,5
        1,4
        0,4
        6,4
        1,1
        6,1
        1,0
        0,5
        1,6
        2,0
        """.trimIndent()

    val testAnswer1 = 22
    check(part1(testInput, 12, 7) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = "6,1"
    check(part2(testInput, 7) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day18")
    // 262
    part1(input, 1024, 71).println()
    // 22,20
    part2(input, 71).println()
}
