package org.juol.aoc.day03

import org.juol.aoc.utils.*

private val NUMBERS_REGEX = Regex("""mul\((\d+),(\d+)\)""")

private fun part1(input: String): Int {
    val sum =
        NUMBERS_REGEX.findAll(input).sumOf {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }
    return sum
}

private fun part2(input: String): Int {
    val parts =
        Regex("""mul\(\d+,\d+\)|(don't\(\))|(do\(\))""")
            .findAll(input)
            .map { it.value }
            .toList()

    var sum = 0
    var enabled = true
    for (part in parts) {
        if (part == "do()") {
            enabled = true
        } else if (part == "don't()") {
            enabled = false
        } else if (enabled) {
            sum +=
                NUMBERS_REGEX
                    .find(part)!!
                    .groupValues
                    .drop(1)
                    .map { it.toInt() }
                    .let { (a, b) -> a * b }
        }
    }

    return sum
}

fun main() {
    val testAnswer1 = 161
    check(
        part1(
            """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """.trimIndent(),
        ) == testAnswer1,
    ) { "answer 1 to test is wrong" }
    val testAnswer2 = 48
    check(
        part2(
            """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """.trimIndent(),
        ) == testAnswer2,
    ) { "answer 2 to test is wrong" }

    val input = readInput("Day03")
    // 157621318
    part1(input).println()
    // 79845780
    part2(input).println()
}
