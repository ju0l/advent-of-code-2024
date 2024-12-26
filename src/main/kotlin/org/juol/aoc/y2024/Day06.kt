package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private fun getVisited(grid: Grid<String>): List<Point>? {
    val visited = mutableListOf<Pair<Direction, Point>>()

    fun walk(
        init: Point,
        direction: Direction,
    ): Boolean {
        var pos = init
        var dir = direction

        val limit = grid.width * grid.height

        while (true) {
            // dumbest way to detect loop
            if (visited.size > limit) {
                return true
            }

            visited.add(Pair(dir, pos))
            val np = pos + dir.dx
            val newChar = grid.getOrNull(np)

            when (newChar) {
                null -> return false
                "#" -> dir = dir.nextClockwise()
                else -> pos = np
            }
        }
    }

    val init = grid.indexOf("^") ?: throw IllegalArgumentException("No '^' found")
    val cycled = walk(init, Direction.UP)
    if (cycled) {
        return null
    }
    return visited.map { it.second }
}

private fun part1(input: String): Int {
    val grid = input.toStringGrid()
    val sum = getVisited(grid)?.toSet()?.size ?: 0
    return sum
}

private fun part2(input: String): Int {
    val grid = input.toStringGrid()
    var sum = 0
    val visited =
        getVisited(grid)?.let { list ->
            list.toMutableSet().also { set -> set.remove(list.first()) }
        }!!

    for (p in visited) {
        val copy = grid.copy()
        copy[p] = "#"

        val newVisited = getVisited(copy)
        if (newVisited == null) {
            sum++
        }
    }

    return sum
}

fun main() {
    val testInput =
        """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
        """.trimIndent()

    val testAnswer1 = 41
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 6
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day06")
    // 5404
    part1(input).println()
    // 1984
    part2(input).println()
}
