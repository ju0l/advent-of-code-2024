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

fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

typealias Point = Pair<Int, Int>

@JvmName("plusInt")
operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)

@JvmName("plusLong")
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

    companion object {
        fun all() = sequenceOf(UP, RIGHT, DOWN, LEFT)
    }
}
