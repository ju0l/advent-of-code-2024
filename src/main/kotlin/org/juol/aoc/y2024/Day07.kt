package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import java.math.BigInteger

private fun count(
    input: List<Pair<Long, List<Long>>>,
    operators: List<String>,
): BigInteger {
    var sum = BigInteger.ZERO
    top@ for ((key, value) in input) {
        val ops =
            cartesianProduct(
                operators,
                operators,
                *(4..value.size).map { operators }.toTypedArray(),
            )
        for (op in ops) {
            val v: Long =
                value.reduceIndexed { i, a, b ->
                    when (op[i - 1]) {
                        "+" -> a + b
                        "*" -> a * b
                        "||" -> "$a$b".toLong()
                        else -> throw IllegalArgumentException("Unknown operator")
                    }
                }
            if (v == key) {
                sum += v.toBigInteger()
                continue@top
            }
        }
    }

    return sum
}

private fun parse(input: String) =
    input.lines().map { row ->
        val (key, value) = row.split(": ")
        key.toLong() to value.split(" ").map { it.toLong() }
    }

private fun part1(input: String): BigInteger = count(parse(input), listOf("+", "*"))

private fun part2(input: String): BigInteger = count(parse(input), listOf("+", "*", "||"))

fun main() {
    val testInput =
        """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
        """.trimIndent()

    val testAnswer1 = 3749.toBigInteger()
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 11387.toBigInteger()
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day07")
    // 4122618559853
    part1(input).println()
    // 227615740238334
    part2(input).println()
}
