package org.juol.aoc.utils

import java.io.File
import java.io.FileWriter

class Grid<T>(
    private val list: MutableList<MutableList<T>>,
    private val separator: String = "",
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

    fun findIndex(predicate: (T) -> Boolean): Point? {
        for (y in list.indices) {
            for (x in list[y].indices) {
                if (predicate(list[y][x])) {
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
                action(x to y, list[y][x])
            }
        }
    }

    fun <R> map(transform: (Point, T) -> R): Grid<R> {
        val newList =
            list
                .mapIndexed { y, row ->
                    row
                        .mapIndexed { x, value ->
                            transform(x to y, value)
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
        fileWriter: FileWriter? = null,
        format: (T) -> String = { it.toString() },
    ) {
        for (row in rows()) {
            val out = row.joinToString(separator) { format(it) }
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
        format: (T) -> String = { it.toString() },
    ) {
        val out = rows().map { row -> row.joinToString(separator) { format(it) } }.joinToString("\n")
        printText(out, image)
    }
}

fun String.toStringGrid(separator: String = ""): Grid<String> {
    val list =
        lines()
            .map { row ->
                row
                    .split(separator)
                    .filter { it.isNotEmpty() }
                    .toMutableList()
            }.toMutableList()
    return Grid(list, separator)
}

fun String.toIntGrid(separator: String = " "): Grid<Int> {
    val list =
        lines()
            .map { row ->
                row
                    .split(separator)
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }
                    .toMutableList()
            }.toMutableList()
    return Grid(list, separator)
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
): Grid<Int> {
    val list =
        MutableList(height) {
            MutableList(width) { value }
        }
    return Grid(list, " ")
}

// DIJKSTRA'S ALGORITHM
typealias Path = List<Point>

data class QueueItem(
    val p: Point,
    val dst: Int,
    val dir: Direction,
    val currentPath: Path,
)

fun <T> Grid<T>.findPaths(
    start: Point,
    end: Point,
    wall: T,
    startDirection: Direction = Direction.UP,
    createDst: (currentPos: Point, currentDst: Int, currentDir: Direction, toDir: Direction) -> Int = { _, dst, _, _ -> dst + 1 },
): Map<Int, List<Path>> {
    val queue = ArrayDeque<QueueItem>().also { it.add(QueueItem(start, 0, startDirection, listOf(start))) }
    val paths = mutableListOf<Pair<Path, Int>>()
    val visited = mutableMapOf<Vector, Int>()

    while (queue.isNotEmpty()) {
        val (currentPos, currentDst, currentDir, currentPath) = queue.removeFirst()

        if (currentPos == end) {
            paths.add(currentPath to currentDst)
            continue
        }
        val currentVector = currentPos to currentDir
        if (visited.getOrDefault(currentVector, Int.MAX_VALUE) < currentDst) {
            continue
        }
        visited[currentVector] = currentDst

        for (dir in Direction.all()) {
            val np = currentPos + dir.dx
            if (!(dir.orientation == currentDir.orientation && dir != currentDir) &&
                np in this &&
                this[np] != wall &&
                np !in currentPath
            ) {
                val newDst = createDst(currentPos, currentDst, currentDir, dir)
                queue.add(QueueItem(np, newDst, dir, currentPath + np))
            }
        }
    }

    return paths.groupBy { it.second }.mapValues { it.value.map { p -> p.first } }
}

fun <T> Grid<T>.findMinPaths(
    start: Point,
    end: Point,
    wall: T,
    startDirection: Direction = Direction.UP,
    createDst: (currentPos: Point, currentDst: Int, currentDir: Direction, toDir: Direction) -> Int = { _, dst, _, _ -> dst + 1 },
): Pair<Int, List<Path>> = findPaths(start, end, wall, startDirection, createDst).minBy { it.key }.toPair()

fun Grid<String>.shortestPath(
    start: Point,
    end: Point,
    wall: String = "#",
): Int {
    val distances = filledGrid(width, height, Int.MAX_VALUE)

    val queue = ArrayDeque<Pair<Point, Int>>()
    queue.add(start to 0)
    distances[start] = 0

    while (queue.isNotEmpty()) {
        val (currentNode, currentDst) = queue.removeFirst()
        Direction.all().map { currentNode + it.dx }.forEach { p ->
            if (p in this &&
                this[p] != wall &&
                currentDst + 1 < distances[p]
            ) {
                distances[p] = currentDst + 1
                queue.add(p to currentDst + 1)
            }
        }
    }

    return distances[end]
}
