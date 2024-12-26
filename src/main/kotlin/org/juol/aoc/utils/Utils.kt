package org.juol.aoc.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

fun readInput(name: String) = Path("src/main/resources/$name.txt").readText()

fun Any?.println() = println(this)

fun Int.isEven() = this % 2 == 0

fun Int.isOdd() = this % 2 != 0

fun Double.isInt() = this % 1.0 == 0.0

fun cartesianProduct(
    a: List<*>,
    b: List<*>,
    vararg sets: List<*>,
): List<List<*>> =
    (listOf(a, b).plus(sets))
        .fold(listOf(listOf<Any?>())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }.toList()

/**
 * Returns all combinations of the given items in list.
 * combinations([1, 2, 3, 4], 3) -> [[1, 2, 3], [1, 2, 4], [1, 3, 4], [2, 3, 4]]
 */
fun <T> combinations(
    int: List<T>,
    length: Int,
): List<List<T>> {
    val combinations = mutableListOf<List<T>>()

    fun generateCombinations(
        start: Int,
        current: List<T>,
    ) {
        if (current.size == length) {
            combinations += current
            return
        }
        for (i in start until int.size) {
            generateCombinations(i + 1, current + int[i])
        }
    }
    generateCombinations(0, emptyList())
    return combinations
}

/**
 * Returns all combinations of the given lists.
 * [[1, 2], [3], [4, 5]] -> [[1, 3, 4], [1, 3, 5], [2, 3, 4], [2, 3, 5]]
 */
fun <T> combineLists(
    paths: List<List<T>>,
    current: List<T> = listOf(),
    index: Int = 0,
): List<List<T>> {
    if (index == paths.size) return listOf(current)
    val results = mutableListOf<List<T>>()
    for (value in paths[index]) {
        val newResults = combineLists(paths, current + value, index + 1)
        results.addAll(newResults)
    }
    return results
}

fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

typealias Point = Pair<Int, Int>

@JvmName("plusIntPair")
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)

@JvmName("plusLongPair")
operator fun Pair<Long, Long>.plus(other: Pair<Long, Long>) = Pair(first + other.first, second + other.second)

val Point.x: Int
    get() = first

val Point.y: Int
    get() = second

enum class Orientation {
    VERTICAL,
    HORIZONTAL,
    NONE,
}

enum class Direction(
    val dx: Point,
    val orientation: Orientation,
) {
    UP(0 to -1, Orientation.VERTICAL),
    RIGHT(1 to 0, Orientation.HORIZONTAL),
    DOWN(0 to 1, Orientation.VERTICAL),
    LEFT(-1 to 0, Orientation.HORIZONTAL),
    NONE(0 to 0, Orientation.NONE),
    ;

    fun nextClockwise(): Direction =
        when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            NONE -> NONE
        }

    companion object {
        fun all() = sequenceOf(UP, RIGHT, DOWN, LEFT)
    }
}
