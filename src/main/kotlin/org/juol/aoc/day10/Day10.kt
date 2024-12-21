package org.juol.aoc.day10

import org.juol.aoc.utils.*

private fun findTrails(
    possibleTrail: List<Point>,
    map: Grid<Int>,
    trails: MutableList<List<Point>>,
) {
    val current = possibleTrail.last()

    val validNeighbours =
        Direction
            .all()
            .map { current + it.dx }
            .filter { dx ->
                (dx in map) && map[current] + 1 == map[dx]
            }

    if (possibleTrail.size == 10) {
        trails.add(possibleTrail)
    }

    validNeighbours.forEach {
        findTrails(possibleTrail + listOf(it), map, trails)
    }
}

private fun part1(input: String): Int {
    val heightMap = input.toIntGrid("")
    val starts = mutableListOf<Point>()

    heightMap.forEach { pair, i ->
        if (i == 0) {
            starts.add(pair)
        }
    }

    var sum = 0

    for (start in starts) {
        val trails: MutableList<List<Point>> = mutableListOf()
        findTrails(listOf(start), heightMap, trails)
        val uniqueTrails = trails.distinctBy { it.last() }
        sum += uniqueTrails.size
    }

    return sum
}

private fun part2(input: String): Int {
    val heightMap = input.toIntGrid("")
    val starts = mutableListOf<Point>()

    heightMap.forEach { p, i ->
        if (i == 0) {
            starts.add(p)
        }
    }

    val sum =
        starts.sumOf { start ->
            val trails: MutableList<List<Point>> = mutableListOf()
            findTrails(listOf(start), heightMap, trails)
            trails.size
        }
    return sum
}

fun main() {
    val testInput =
        """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
        """.trimIndent()

    val testAnswer1 = 36
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 81
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day10")
    // 538
    part1(input).println()
    // 1110
    part2(input).println()
}
