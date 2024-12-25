package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private fun parse(input: String): Pair<List<List<Int>>, List<List<Int>>> {
    val locks = mutableListOf<List<Int>>()
    val keys = mutableListOf<List<Int>>()

    input.split("\n\n").map { it.toStringGrid().columns() }.forEach { columns ->
        val n = columns.map { c -> c.count { it == "#" } - 1 }
        if (columns.first().first() == "#") {
            locks.add(n)
        } else {
            keys.add(n)
        }
    }

    return locks to keys
}

private fun part1(input: String): Int {
    val (locks, keys) = parse(input)

    var sum = 0
    for (lock in locks) {
        for (key in keys) {
            val overlap = lock.zip(key).all { (l, k) -> k + l < 6 }
            if (overlap) {
                sum++
            }
        }
    }

    return sum
}

fun main() {
    val testInput =
        """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....

        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....

        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####

        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####

        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        #####
        """.trimIndent()

    val testAnswer1 = 3
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }

    val input = readInput("Day25")
    part1(input).println()
}
