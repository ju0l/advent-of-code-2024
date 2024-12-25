package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.abs

private data class Fence(
    val p: Point,
    val direction: Direction,
)

private fun getRegion(
    matrix: Grid<String>,
    candidates: MutableList<Point>,
): MutableSet<Point> {
    val region = mutableSetOf<Point>()
    while (candidates.isNotEmpty()) {
        val candidate = candidates.removeFirst()
        region.add(candidate)

        Direction
            .all()
            .map { candidate + it.dx }
            .filter { matrix.getOrNull(it) == matrix[candidate] }
            .forEach {
                if (!region.contains(it) && !candidates.contains(it)) {
                    candidates.add(it)
                }
            }
    }
    return region
}

private fun getRegions(matrix: Grid<String>): List<Set<Point>> {
    val regions = mutableListOf<MutableSet<Point>>()
    matrix.forEach { p, _ ->
        if (regions.find { it.contains(p) } == null) {
            regions.add(getRegion(matrix, mutableListOf(p)))
        }
    }
    return regions
}

private fun Point.getFences(matrix: Grid<String>): List<Fence> =
    Direction
        .all()
        .map { Fence(this + it.dx, it) }
        .filter { (p) -> matrix.getOrNull(p) != matrix[this] }

private fun Set<Point>.getFences(matrix: Grid<String>): List<Fence> = this.map { it.getFences(matrix) }.flatten()

private fun part1(input: String): Int {
    val matrix = input.toStringGrid()
    val regions = getRegions(matrix)
    val sum =
        regions.sumOf { region ->
            region.getFences(matrix).size * region.size
        }

    return sum
}

private fun part2(input: String): Int {
    val matrix = input.toStringGrid()
    val regions = getRegions(matrix)
    var sum = 0
    for (region in regions) {
        val fences = region.getFences(matrix)
        var fenceCount = 0

        // vertical fences
        val vertical =
            fences
                .filter { it.direction.orientation != Orientation.VERTICAL }
                .groupBy { "${it.p.x}_${it.direction}" }
                .filterValues { it.isNotEmpty() }
        for (vf in vertical) {
            val positions =
                vf.value
                    .map { it.p.y }
                    .sorted()

            fenceCount++
            if (positions.size > 1) {
                positions.zipWithNext { a, b ->
                    if (abs(a - b) > 1) {
                        fenceCount++
                    }
                }
            }
        }

        // horizontal fences
        val horizontal =
            fences
                .filter { it.direction.orientation != Orientation.HORIZONTAL }
                .groupBy { "${it.p.y}_${it.direction}" }
                .filterValues { it.isNotEmpty() }
        for (hf in horizontal) {
            val positions =
                hf.value
                    .map { it.p.x }
                    .sorted()

            fenceCount++
            if (positions.size > 1) {
                positions.zipWithNext { a, b ->
                    if (abs(a - b) > 1) {
                        fenceCount++
                    }
                }
            }
        }
        sum += fenceCount * region.size
    }
    return sum
}

fun main() {
    val testInput1 =
        """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
        """.trimIndent()

    val testInput2 =
        """
        EEEEE
        EXXXX
        EEEEE
        EXXXX
        EEEEE
        """.trimIndent()

    val testAnswer1 = 1930
    check(part1(testInput1) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 236
    check(part2(testInput2) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day12")
    // 1375574
    part1(input).println()
    // 830566
    part2(input).println()
}
