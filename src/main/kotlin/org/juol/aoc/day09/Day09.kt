package org.juol.aoc.day09

import org.juol.aoc.utils.*

private enum class BlockType {
    DATA,
    GAP,
}

private data class Block(
    val id: Int,
    val size: Int,
    val type: BlockType,
) {
    override fun toString(): String =
        when (type) {
            BlockType.DATA -> "$id"
            BlockType.GAP -> "."
        }.repeat(size)
}

private fun String.toDiskBlock(): MutableList<Block> =
    this
        .split("")
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .mapIndexed { i, v ->
            when (i % 2) {
                0 -> Block(id = i / 2, size = v, type = BlockType.DATA)
                else -> Block(id = -1, size = v, type = BlockType.GAP)
            }
        }.toMutableList()

private fun String.toDisk(): MutableList<Int> =
    this
        .split("")
        .asSequence()
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .mapIndexed { i, v ->
            when (i % 2) {
                0 -> List(v) { i / 2 }
                else -> List(v) { -1 }
            }
        }.flatten()
        .toMutableList()

private fun MutableList<Block>.sumIds(): Long {
    var idx = 0
    var sum = 0L
    for (block in this) {
        for (i in 0..<block.size) {
            if (block.type == BlockType.DATA) {
                sum += idx * block.id
            }
            idx++
        }
    }
    return sum
}

private fun part1(input: String): Long {
    val disk = input.toDisk()

    for (i in disk.indices) {
        val dotIndex = disk.indexOf(-1)
        val idIndex = disk.indexOfLast { it != -1 }

        disk[dotIndex] = disk[idIndex]
        disk[idIndex] = -1
    }

    val sum: Long =
        disk.filter { it >= 0 }.foldIndexed(0) { i, acc, v -> acc + i * v }

    return sum
}

private fun part2(input: String): Long {
    val disk = input.toDiskBlock()
    val highestId = disk.maxOf { it.id }

    for (id in highestId downTo 0) {
        val lastDataIndex = disk.indexOfFirst { it.id == id }
        val lastData = disk[lastDataIndex]
        val firstDotIndex = disk.indexOfFirst { it.type == BlockType.GAP && it.size >= lastData.size }

        if (firstDotIndex in 0..<lastDataIndex) {
            disk.removeAt(lastDataIndex)
            disk[firstDotIndex] = disk[firstDotIndex].copy(size = disk[firstDotIndex].size - lastData.size)
            disk.add(firstDotIndex, lastData)
            disk[lastDataIndex] = disk[lastDataIndex].copy(size = disk[lastDataIndex].size + lastData.size)
        }
    }

    val sum = disk.sumIds()
    return sum
}

fun main() {
    val testInput1 =
        """
        2333133121414131402
        """.trimIndent()

    val testAnswer1 = 1928L
    check(part1(testInput1) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 2858L
    check(part2(testInput1) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day09")
    // 6398252054886
    part1(input).println()
    // 6415666220005
    part2(input).println()
}
