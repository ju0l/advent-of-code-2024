package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private data class Machine(
    val a: Pair<Long, Long>,
    val b: Pair<Long, Long>,
    val prize: Pair<Long, Long>,
)

private fun parseRow(
    row: String,
    separator: String,
): Pair<Long, Long> =
    row
        .split(": ")[1]
        .split(",")
        .map { it.split(separator)[1].toLong() }
        .let { it[0] to it[1] }

private fun parseMachine(
    a: String,
    b: String,
    prize: String,
    add: Long,
): Machine = Machine(parseRow(a, "+"), parseRow(b, "+"), parseRow(prize, "=") + (add to add))

private fun parse(
    input: String,
    add: Long = 0L,
): List<Machine> =
    input.lines().windowed(4, 4, true).map {
        parseMachine(it[0], it[1], it[2], add)
    }

private fun calc(machine: Machine): Pair<Double, Double> {
    // 2 equations system
    val (ax, ay) = machine.a
    val (bx, by) = machine.b
    val (px, py) = machine.prize
    val bCount = (py * ax - px * ay).toDouble() / (by * ax - bx * ay).toDouble()
    val aCount = (px - bx * bCount).toDouble() / ax.toDouble()

    return aCount to bCount
}

private fun part1(input: String): Long {
    val machines = parse(input)

    var sum = 0L
    for (machine in machines) {
        val (aCount, bCount) = calc(machine)
        if (aCount.isInt() && bCount.isInt() && aCount.toLong() in 0..100 && bCount.toLong() in 0..100) {
            sum += (aCount.toLong() * 3) + bCount.toLong()
        }
    }
    return sum
}

private fun part2(input: String): Long {
    val machines = parse(input, 10000000000000L)

    var sum = 0L
    for (machine in machines) {
        val (aCount, bCount) = calc(machine)
        if (aCount.isInt() && bCount.isInt() && aCount >= 0L && bCount >= 0L) {
            sum += (aCount.toLong() * 3) + bCount.toLong()
        }
    }
    return sum
}

fun main() {
    val testInput =
        """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
        """.trimIndent()

    val testAnswer1 = 480L
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }

    val input = readInput("Day13")
    // 31065
    part1(input).println()
    // 93866170395343
    part2(input).println()
}
