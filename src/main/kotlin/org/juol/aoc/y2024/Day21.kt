package org.juol.aoc.y2024

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.juol.aoc.utils.*
import java.util.concurrent.ConcurrentHashMap

private val numericKeypad =
    """
789
456
123
#0A
    """.trimIndent().toStringGrid()
private val arrowKeypad =
    """
#^A
<v>
    """.trimIndent().toStringGrid()

private val numberKeypadPaths = buildKeypadPaths(numericKeypad)
private val arrowKeypadPaths = buildKeypadPaths(arrowKeypad)

private fun movement(
    form: Point,
    to: Point,
): String {
    val dx = to.x - form.x
    val dy = to.y - form.y
    return if (dx != 0) {
        if (dx > 0) {
            ">"
        } else {
            "<"
        }
    } else {
        if (dy > 0) {
            "v"
        } else {
            "^"
        }
    }
}

private fun charCommands(
    keypad: Grid<String>,
    start: Point,
    end: Point,
): List<GridPath> = keypad.findMinPaths(start, end, "#")

private fun buildKeypadPaths(keypad: Grid<String>): Map<Pair<String, String>, List<String>> {
    val pos =
        sortedMapOf<String, Pair<Int, Int>>().also {
            keypad.forEach { p, s ->
                it[s] = p
            }
        }

    val moves = mutableMapOf<Pair<String, String>, MutableList<String>>()
    for (key1 in pos.keys) {
        for (key2 in pos.keys) {
            if (key1 != "#" && key2 != "#" && key1 != key2) {
                val paths =
                    charCommands(keypad, pos[key1]!!, pos[key2]!!)
                        .map { p -> p.path.zipWithNext { a, b -> movement(a.p, b.p) }.joinToString("") + "A" }
                moves
                    .getOrPut(Pair(key1, key2)) { mutableListOf() }
                    .addAll(paths)
            }
        }
    }

    return moves
}

private fun combinations(
    paths: List<List<String>>,
    current: List<String> = listOf(),
    index: Int = 0,
): List<List<String>> {
    if (index == paths.size) return listOf(current)
    val results = mutableListOf<List<String>>()
    for (value in paths[index]) {
        val newResults = combinations(paths, current + value, index + 1)
        results.addAll(newResults)
    }
    return results
}

private val cache = ConcurrentHashMap<Pair<String, Int>, Long>()

private fun getMoves(
    sequence: String,
    depth: Int,
): Long {
    val cacheKey = sequence to depth
    if (!cache.containsKey(cacheKey)) {
        val keypadPaths = if (sequence[0].isDigit()) numberKeypadPaths else arrowKeypadPaths
        val moves =
            "A$sequence".zipWithNext { a, b ->
                if (a != b) keypadPaths[(a.toString() to b.toString())]!! else listOf("A")
            }
        val paths = combinations(moves)
        cache[cacheKey] =
            if (depth == 0) {
                paths.minOf { it.sumOf { move -> move.length.toLong() } }
            } else {
                paths.minOf { it.sumOf { currCode -> getMoves(currCode, depth - 1) } }
            }
    }

    return cache[cacheKey]!!
}

private fun runParallel(
    input: String,
    depth: Int,
): Long =
    runBlocking(Dispatchers.IO) {
        input
            .lines()
            .map { row ->
                async {
                    getMoves(row, depth) * row.dropLast(1).toInt()
                }
            }.awaitAll()
            .sum()
    }

private fun part1(input: String): Long = runParallel(input, 2)

private fun part2(input: String): Long = runParallel(input, 25)

fun main() {
    val testInput =
        """
        029A
        980A
        179A
        456A
        379A
        """.trimIndent()

    val testAnswer1 = 126384L
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
//    val testAnswer2 = 0
//    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day21")
    // 136780
    part1(input).println()
    // 167538833832712
    part2(input).println()
}
