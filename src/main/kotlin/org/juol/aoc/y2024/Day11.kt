package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private fun MutableMap<Long, Long>.blink(): MutableMap<Long, Long> {
    val stones = mutableMapOf<Long, Long>()
    for ((stone, count) in this) {
        val newStones =
            if (stone == 0L) {
                listOf(1L)
            } else {
                val stringStone = stone.toString()
                if (stringStone.length.isEven()) {
                    listOf(
                        stringStone.take(stringStone.length / 2).toLong(),
                        stringStone.takeLast(stringStone.length / 2).toLong(),
                    )
                } else {
                    listOf(stone * 2024L)
                }
            }
        for (ns in newStones) {
            stones[ns] = stones.getOrDefault(ns, 0) + count
        }
    }

    return stones
}

private fun process(
    repeats: Int,
    input: String,
): Long {
    var stones =
        mutableMapOf<Long, Long>().apply {
            input.split(" ").forEach {
                val l = it.toLong()
                this[l] = this.getOrDefault(l, 0) + 1
            }
        }

    for (i in 1..repeats) {
        stones = stones.blink()
    }
    val sum = stones.values.sum()
    return sum
}

private fun part1(input: String): Long = process(25, input)

private fun part2(input: String): Long = process(75, input)

fun main() {
    val testInput =
        """
        125 17
        """.trimIndent()

    val testAnswer1 = 55312L
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    // test 2 is not relevant

    val input = readInput("Day11")
    // 231278
    part1(input).println()
    // 274229228071551
    part2(input).println()
}
