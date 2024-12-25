package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.abs

private fun findCheats(
    path: List<Point>,
    minSavedSteps: Int,
    cheatMoves: Int,
): Int {
    val pathDistances = mutableMapOf<Point, Int>()
    path.forEachIndexed { i, p ->
        pathDistances[p] = i
    }

    val vectors = mutableListOf<Pair<Point, Int>>()
    for (dx in -cheatMoves..cheatMoves) {
        for (dy in -cheatMoves..cheatMoves) {
            if (dx == 0 && dy == 0) continue
            val distance = abs(dx) + abs(dy)
            if (distance <= cheatMoves) {
                vectors.add(Pair(dx to dy, distance))
            }
        }
    }
    var cheats = 0
    for (p in path) {
        for ((d, dst) in vectors) {
            val np = p + d
            if (pathDistances.containsKey(np) &&
                minSavedSteps <= (pathDistances[np]!! - pathDistances[p]!! - dst)
            ) {
                cheats += 1
            }
        }
    }
    return cheats
}

private fun part1(
    input: String,
    minSavedSteps: Int,
): Int {
    val grid = input.toStringGrid()
    val start = grid.indexOf("S") ?: error("No start found")
    val end = grid.indexOf("E") ?: error("No end found")
    val (_, path) = grid.findMinPaths(start, end, "#", Direction.UP)

    val sum = findCheats(path.first(), minSavedSteps, 2)
    return sum
}

private fun part2(
    input: String,
    minSavedSteps: Int,
): Int {
    val grid = input.toStringGrid()
    val start = grid.indexOf("S") ?: error("No start found")
    val end = grid.indexOf("E") ?: error("No end found")
    val (_, path) = grid.findMinPaths(start, end, "#", Direction.UP)

    val sum = findCheats(path.first(), minSavedSteps, 20)
    return sum
}

fun main() {
    val testInput =
        """
        ###############
        #...#...#.....#
        #.#.#.#.#.###.#
        #S#...#.#.#...#
        #######.#.#.###
        #######.#.#...#
        #######.#.###.#
        ###..E#...#...#
        ###.#######.###
        #...###...#...#
        #.#####.#.###.#
        #.#...#.#.#...#
        #.#.#.#.#.#.###
        #...#...#...###
        ###############
        """.trimIndent()

    val testAnswer1 = 1
    check(part1(testInput, 64) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 3
    check(part2(testInput, 76) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day20")
    part1(input, 100).println()
    part2(input, 100).println()
}
