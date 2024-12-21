package org.juol.aoc.day19

import org.juol.aoc.utils.*

private fun matchCount(
    towels: List<String>,
    stripe: String,
): Long {
    val cache = mutableMapOf<String, Long>()

    fun proc(
        towels: List<String>,
        stripe: String,
    ): Long {
        val count =
            cache[stripe] ?: if (stripe.isEmpty()) {
                1L
            } else {
                towels.filter { stripe.startsWith(it) }.sumOf {
                    proc(towels, stripe.removePrefix(it))
                }
            }
        cache[stripe] = count
        return count
    }

    return proc(towels, stripe)
}

private fun parse(input: String): Pair<List<String>, List<String>> {
    val towels = input.lines()[0].split(", ").filter { it.isNotBlank() }
    val stripes = input.lines().drop(2)
    return towels to stripes
}

private fun part1(input: String): Int {
    val (towels, stripes) = parse(input)
    val sum = stripes.count { matchCount(towels, it) != 0L }
    return sum
}

private fun part2(input: String): Long {
    val (towels, stripes) = parse(input)
    val sum = stripes.sumOf { matchCount(towels, it) }
    return sum
}

fun main() {
    val testInput =
        """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
        """.trimIndent()

    val testAnswer1 = 6
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 16L
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day19")
    // 258
    part1(input).println()
    // 632423618484345
    part2(input).println()
}
