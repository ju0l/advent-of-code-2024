package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.floor

private fun ruleApplies(
    change: List<Int>,
    rule: List<Int>,
): Boolean {
    val (a, b) = rule
    return change.contains(a) && change.contains(b) && change.indexOf(a) > change.indexOf(b)
}

private fun isCorrect(
    change: List<Int>,
    rules: List<List<Int>>,
): Boolean {
    for (rule in rules) {
        val (a, b) = rule
        if (ruleApplies(change, rule)) {
            return false
        }
    }
    return true
}

private fun middleNumber(change: List<Int>): Int = change[floor(change.size / 2.0).toInt()]

private fun parseInput(input: String): Pair<List<List<Int>>, List<List<Int>>> {
    val rules = input.split("\n\n")[0].lines().map { it.split("|").map { it.toInt() } }
    val changes = input.split("\n\n")[1].lines().map { it.split(",").map { it.toInt() } }
    return rules to changes
}

private fun part1(input: String): Int {
    val (rules, changes) = parseInput(input)

    val sum = changes.filter { change -> isCorrect(change, rules) }.sumOf { middleNumber(it) }
    return sum
}

private fun part2(input: String): Int {
    val (rules, changes) = parseInput(input)

    fun fixChange(change: List<Int>): List<Int> {
        val fixedChange = change.toMutableList()
        for (rule in rules) {
            val (a, b) = rule
            if (ruleApplies(fixedChange, rule)) {
                val aIndex = fixedChange.indexOf(a)
                val bIndex = fixedChange.indexOf(b)
                fixedChange.removeAt(bIndex)
                fixedChange.add(aIndex, b)
            }
        }
        if (!isCorrect(fixedChange, rules)) {
            return fixChange(fixedChange)
        }
        return fixedChange
    }

    val sum = changes.filter { change -> !isCorrect(change, rules) }.sumOf { middleNumber(fixChange(it)) }
    return sum
}

fun main() {
    val testInput =
        """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13
        
        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
        """.trimIndent()

    val testAnswer1 = 143
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 123
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day05")
    // 5208
    part1(input).println()
    // 6732
    part2(input).println()
}
