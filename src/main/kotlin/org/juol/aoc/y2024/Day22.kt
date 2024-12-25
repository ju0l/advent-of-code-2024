package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import java.math.BigInteger
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val cache = mutableMapOf<Long, Long>()
private val lock = ReentrantLock()

private fun Long.generateNext(): Long {
    var cached = cache[this]
    if (cached == null) {
        var s = this.toBigInteger()
        var n = s * 64.toBigInteger()
        s = s.xor(n)
        s %= 16777216.toBigInteger()
        n = s / 32.toBigInteger()
        s = s.xor(n)
        s %= 16777216L.toBigInteger()
        n = s * 2048.toBigInteger()
        s = s.xor(n)
        s %= 16777216L.toBigInteger()
        cached = s.toLong()
        lock.withLock {
            cache[this] = cached
        }
    }
    return cached
}

private fun parse(input: String): List<Long> = input.lines().map { it.toLong() }

private fun part1(input: String): BigInteger {
    var sum = BigInteger.ZERO
    for (buyer in parse(input)) {
        var s = buyer
        for (i in 0 until 2000) {
            s = s.generateNext()
        }
        sum += s.toBigInteger()
    }
    return sum
}

private fun part2(input: String): Int {
    val allPrices = mutableListOf<List<Int>>()
    for (secret in parse(input)) {
        val prices = mutableListOf<Int>()
        var s = secret
        for (i in 0 until 2000) {
            s = s.generateNext()
            prices.add((s % 10).toInt())
        }
        allPrices.add(prices)
    }
    val allDiffs = allPrices.map { p -> p.zipWithNext { a, b -> b - a } }

    val bananas = mutableMapOf<List<Int>, Int>()
    for ((buyerIdx, diffs) in allDiffs.withIndex()) {
        val sequences = mutableSetOf<List<Int>>()
        for (i in 0..diffs.size - 4) {
            val seq = diffs.subList(i, i + 4)
            if (seq in sequences) continue
            bananas[seq] = bananas.getOrDefault(seq, 0) + allPrices[buyerIdx][i + 4]
            sequences.add(seq)
        }
    }
    val max = bananas.values.max()
    return max
}

fun main() {
    val testInput1 =
        """
        1
        10
        100
        2024
        """.trimIndent()

    val testInput2 =
        """
        1
        2
        3
        2024
        """.trimIndent()

    val testAnswer1 = 37327623.toBigInteger()
    check(part1(testInput1) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 23
    check(part2(testInput2) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day22")
    // 12664695565
    part1(input).println()
    // 1444
    part2(input).println()
}
