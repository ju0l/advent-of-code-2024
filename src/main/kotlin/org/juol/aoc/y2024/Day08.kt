package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.abs

private fun calcNodes(
    pos1: Point,
    pos2: Point,
    grid: Grid<String>,
    wholeLine: Boolean = false,
): List<Point> {
    val (x1, y1) = pos1
    val (x2, y2) = pos2
    val xDiff = abs(x1 - x2)
    val yDiff = abs(y1 - y2)

    val antinodes = mutableListOf<Point>()

    val n =
        when {
            // first is top left from second
            x1 < x2 && y1 < y2 -> pos1 + (-xDiff to -yDiff)
            // first is top right from second
            x1 > x2 && y1 < y2 -> pos1 + (xDiff to -yDiff)
            // first is bottom left from second
            x1 < x2 && y1 > y2 -> pos1 + (-xDiff to yDiff)
            // first is bottom right from second
            x1 > x2 && y1 > y2 -> pos1 + (xDiff to yDiff)
            else -> null
        }

    if (n != null && n in grid) {
        antinodes.add(n)
        if (wholeLine) {
            antinodes.addAll(calcNodes(n, pos1, grid, wholeLine))
        }
    }

    return antinodes
}

private fun groupNodes(grid: Grid<String>): Map<String, MutableList<Point>> {
    val nodes = mutableMapOf<String, MutableList<Point>>()
    grid.forEach { p, node ->
        if (node != ".") {
            if (node !in nodes) {
                nodes[node] = mutableListOf()
            }
            nodes[node]!!.add(p)
        }
    }

    return nodes
}

private fun part1(input: String): Int {
    val grid = input.toStringGrid()
    val antinodes = filledGrid(grid.width, grid.height, ".")
    val nodes = groupNodes(grid)

    for (positions in nodes.values) {
        for (pos1 in positions) {
            for (pos2 in positions) {
                calcNodes(pos1, pos2, grid).forEach { antinodes[it] = "#" }
            }
        }
    }

    val sum = antinodes.count { it == "#" }
    return sum
}

private fun part2(input: String): Int {
    val grid = input.toStringGrid()
    val antinodes = grid.copy()
    val nodes = groupNodes(grid)

    for (positions in nodes.values) {
        for (pos1 in positions) {
            for (pos2 in positions) {
                calcNodes(pos1, pos2, grid, true).forEach { antinodes[it] = "#" }
            }
        }
    }

    val sum = antinodes.count { it != "." }
    return sum
}

fun main() {
    val testInput =
        """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
        """.trimIndent()

    val testAnswer1 = 14
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 34
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day08")

    // 269
    part1(input).println()
    // 949
    part2(input).println()
}
