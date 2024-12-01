package org.juol.aoc

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(name: String) = Path("src/main/resources/$name.txt").readLines()

fun parseIntColumns(input: List<String>): List<List<Int>> {
    val columns = mutableListOf<List<Int>>()

    input
        .map { it.split("\\s+".toRegex()) }
        .map { row -> row.filter { it.isNotBlank() }.map { it.toInt() } }
        .forEach {
            it.forEachIndexed { idx, value ->
                if (columns.size <= idx) {
                    columns.add(listOf())
                }
                columns[idx] = columns[idx] + value
            }
        }

    return columns
}

fun String.md5() =
    BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

fun Any?.println() = println(this)
