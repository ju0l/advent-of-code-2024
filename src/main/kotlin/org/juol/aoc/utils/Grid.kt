package org.juol.aoc.utils

import java.io.File
import java.io.FileWriter

class Grid<T>(
    private val list: MutableList<MutableList<T>>,
    private val separator: String = "",
    private val formatter: (T) -> String = { it.toString() },
) {
    val width: Int
        get() = list.getOrNull(0)?.size ?: 0

    val height: Int
        get() = list.size

    val xIndices: IntRange
        get() = 0 until width

    val yIndices: IntRange
        get() = 0 until height

    operator fun get(
        x: Int,
        y: Int,
    ): T = list[y][x]

    operator fun get(p: Point): T = get(p.x, p.y)

    fun getOrNull(
        x: Int,
        y: Int,
    ): T? = list.getOrNull(y)?.getOrNull(x)

    fun getOrNull(p: Point): T? = getOrNull(p.x, p.y)

    operator fun set(
        x: Int,
        y: Int,
        value: T,
    ) {
        list[y][x] = value
    }

    operator fun set(
        p: Point,
        value: T,
    ) {
        set(p.x, p.y, value)
    }

    operator fun contains(p: Point): Boolean = p.x in xIndices && p.y in yIndices

    operator fun contains(value: T): Boolean = list.any { it.contains(value) }

    fun indexOf(element: T): Point? {
        for (y in list.indices) {
            for (x in list[y].indices) {
                if (list[y][x] == element) {
                    return Point(x, y)
                }
            }
        }
        return null
    }

    fun findIndex(predicate: (Point, T) -> Boolean): Point? {
        for (y in list.indices) {
            for (x in list[y].indices) {
                if (predicate(Point(x, y), list[y][x])) {
                    return Point(x, y)
                }
            }
        }
        return null
    }

    fun rows(): List<List<T>> = list

    fun columns(): List<List<T>> {
        val columns = mutableListOf<MutableList<T>>()
        for (x in xIndices) {
            columns.add(mutableListOf())
            for (y in yIndices) {
                columns[x].add(list[y][x])
            }
        }
        return columns
    }

    fun leftDiagonals(): List<List<T>> {
        val diagonals = mutableListOf<MutableList<T>>()
        for (k in 0 until width + height - 1) {
            diagonals.add(mutableListOf())
            for (j in 0..k) {
                val i = k - j
                if (i in xIndices && j in yIndices) {
                    diagonals[k].add(list[j][i])
                }
            }
        }
        return diagonals
    }

    fun rightDiagonals(): List<List<T>> {
        val diagonals = mutableListOf<MutableList<T>>()
        for (k in 0 until width + height - 1) {
            diagonals.add(mutableListOf())
            for (j in 0..k) {
                val i = k - j
                if (i in xIndices && j in yIndices) {
                    diagonals[k].add(list[j][width - 1 - i])
                }
            }
        }
        return diagonals
    }

    fun forEach(action: (Point, T) -> Unit) {
        for (y in list.indices) {
            for (x in list[y].indices) {
                action(Point(x, y), list[y][x])
            }
        }
    }

    fun <R> map(transform: (Point, T) -> R): Grid<R> {
        val newList =
            list
                .mapIndexed { y, row ->
                    row
                        .mapIndexed { x, value ->
                            transform(Point(x, y), value)
                        }.toMutableList()
                }.toMutableList()
        return Grid(newList)
    }

    fun count(condition: (T) -> Boolean) = rows().sumOf { row -> row.count { condition(it) } }

    fun sumOf(condition: (x: Int, y: Int, T) -> Long): Long {
        var sum = 0L
        forEach { (x, y), s ->
            sum += condition(x, y, s)
        }
        return sum
    }

    fun copy(): Grid<T> {
        val newList =
            list
                .map { it.toMutableList() }
                .toMutableList()
        return Grid(newList)
    }

    fun print(
        separator: String = this.separator,
        formatter: (T) -> String = this.formatter,
        fileWriter: FileWriter? = null,
    ) {
        for (row in rows()) {
            val out = row.joinToString(separator) { formatter(it) }
            if (fileWriter != null) {
                fileWriter.append(out)
                fileWriter.append("\n")
            } else {
                println(out)
            }
        }
    }

    fun printToImage(
        image: File,
        separator: String = this.separator,
        formatter: (T) -> String = this.formatter,
    ) {
        val out = rows().joinToString("\n") { row -> row.joinToString(separator) { formatter(it) } }
        printText(out, image)
    }
}

fun String.toStringGrid(separator: String = ""): Grid<String> {
    val list =
        lines()
            .map { row ->
                row
                    .split(separator)
                    .filter { it.isNotBlank() }
                    .toMutableList()
            }.toMutableList()
    return Grid(list, separator)
}

fun String.toIntGrid(separator: String = " "): Grid<Int> {
    var max = 0
    val list =
        lines()
            .map { row ->
                row
                    .split(separator)
                    .filter { it.isNotBlank() }
                    .map { it.toInt().also { max = it.coerceAtLeast(max) } }
                    .toMutableList()
            }.toMutableList()
    return Grid(list, separator, intFormatter(max.toString().length))
}

fun filledGrid(
    width: Int,
    height: Int,
    value: String,
): Grid<String> {
    val list =
        MutableList(height) {
            MutableList(width) { value }
        }
    return Grid(list, "")
}

fun filledGrid(
    width: Int,
    height: Int,
    value: Int,
    formatDigits: Int = value.toString().length,
): Grid<Int> {
    val list =
        MutableList(height) {
            MutableList(width) { value }
        }
    return Grid(list, " ", intFormatter(formatDigits))
}

fun intFormatter(decimals: Int): (Int) -> String = { it.toString().padStart(decimals, ' ') }
