package org.juol.aoc.y2024

import org.juol.aoc.utils.*
import kotlin.math.pow

private fun execute(
    registers: List<Long>,
    program: List<Long>,
): List<Long> {
    var (a, b, c) = registers
    var pointer = 0
    val outputs = mutableListOf<Long>()

    fun combo(value: Long) =
        when (value) {
            0L, 1L, 2L, 3L -> value
            4L -> a
            5L -> b
            6L -> c
            else -> throw IllegalArgumentException("Invalid operand $value")
        }

    while (pointer < program.size) {
        val opcode = program[pointer]
        val operand = program[pointer + 1]

        when (opcode) {
            0L -> a = (a / 2.0.pow(combo(operand).toDouble())).toLong()
            1L -> b = b.xor(operand)
            2L -> b = combo(operand) % 8
            3L -> {
                if (a != 0L) {
                    pointer = operand.toInt()
                    continue
                }
            }

            4L -> b = b.xor(c)
            5L -> outputs.add(combo(operand) % 8)
            6L -> b = (a / 2.0.pow(combo(operand).toDouble())).toLong()
            7L -> c = (a / 2.0.pow(combo(operand).toDouble())).toLong()
        }

        pointer += 2
    }

    return outputs
}

private fun part1(input: String): String {
    val registers = input.lines().take(3).map { it.split(": ")[1].toLong() }

    val program =
        input
            .lines()[4]
            .split(": ")[1]
            .split(",")
            .map { it.toLong() }

    return execute(registers, program).joinToString(",")
}

private fun part2(input: String): Long {
    val program =
        input
            .lines()[4]
            .split(": ")[1]
            .split(",")
            .map { it.toLong() }

    var idx = (0..<program.size - 1).sumOf { 7 * 8.0.pow(it.toDouble()).toLong() } + 1

    while (true) {
        val p: List<Long> = execute(listOf(idx, 0, 0), program)
        if (p == program) {
            return idx
        }

        for (i in program.indices.reversed()) {
            if (p[i] != program[i]) {
                idx += 8.0.pow(i.toDouble()).toLong()
                break
            }
        }
    }
}

fun main() {
    val testInput1 =
        """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
        """.trimIndent()

    val testInput2 =
        """
        Register A: 2024
        Register B: 0
        Register C: 0

        Program: 0,3,5,4,3,0
        """.trimIndent()

    val testAnswer1 = "4,6,3,5,6,3,5,2,1,0"
    check(part1(testInput1) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 117440L
    check(part2(testInput2) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day17")
    // 1,4,6,1,6,4,3,0,3
    part1(input).println()
    // 265061364597659
    part2(input).println()
}
