package org.juol.aoc.utils

data class PathStep(
    val p: Point,
    val dst: Int,
    val dir: Direction,
)

class GridPath private constructor(
    val path: List<PathStep>,
) {
    constructor(step: PathStep) : this(listOf(step))

    val start
        get() = path.firstOrNull()?.p ?: throw IllegalArgumentException("Path is empty")
    val end
        get() = path.lastOrNull()?.p ?: throw IllegalArgumentException("Path is empty")
    val dst
        get() = path.lastOrNull()?.dst ?: 0
    val dir
        get() = path.lastOrNull()?.dir ?: Direction.NONE

    operator fun plus(step: PathStep): GridPath = GridPath(path + step)

    operator fun contains(p: Point): Boolean = path.any { it.p == p }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return path == (other as GridPath).path
    }

    override fun hashCode(): Int = path.hashCode()
}

typealias Vector = Pair<Point, Direction>

fun <T> Grid<T>.findPaths(
    start: Point,
    end: Point,
    wall: T,
    startDirection: Direction = Direction.NONE,
    createDst: (
        currentDst: Int,
        currentPos: Point,
        toPos: Point,
        currentDir: Direction,
        toDir: Direction,
    ) -> Int = { dst, _, _, _, _ -> dst + 1 },
): List<GridPath> {
    val queue =
        ArrayDeque<GridPath>().also {
            it.add(GridPath(PathStep(start, 0, startDirection)))
        }
    val paths = mutableListOf<GridPath>()
    val visited = mutableMapOf<Vector, Int>()

    while (queue.isNotEmpty()) {
        val currentPath = queue.removeFirst()

        if (currentPath.end == end) {
            paths.add(currentPath)
            continue
        }
        val currentVector = currentPath.end to currentPath.dir
        if (visited.getOrDefault(currentVector, Int.MAX_VALUE) < currentPath.dst) {
            continue
        }
        visited[currentVector] = currentPath.dst

        for (dir in Direction.all()) {
            val newPos = currentPath.end + dir.dx
            if ((dir == currentPath.dir || dir.orientation != currentPath.dir.orientation) &&
                newPos in this &&
                this[newPos] != wall &&
                newPos !in currentPath
            ) {
                val newDir = createDst(currentPath.dst, currentPath.end, newPos, currentPath.dir, dir)
                queue.add(currentPath + PathStep(newPos, newDir, dir))
            }
        }
    }

    return paths
}

fun <T> Grid<T>.findMinPaths(
    start: Point,
    end: Point,
    wall: T,
    startDirection: Direction = Direction.NONE,
    createDst: (
        currentDst: Int,
        currentPos: Point,
        toPos: Point,
        currentDir: Direction,
        toDir: Direction,
    ) -> Int = { dst, _, _, _, _ -> dst + 1 },
): List<GridPath> {
    val allPaths = findPaths(start, end, wall, startDirection, createDst)
    if (allPaths.isEmpty()) return emptyList()
    val minDst = allPaths.minOf { it.dst }
    return allPaths.filter { it.dst == minDst }
}

fun <T> Grid<T>.shortestDst(
    start: Point,
    end: Point,
    wall: T,
): Int {
    val distances = filledGrid(width, height, Int.MAX_VALUE, 2)
    val queue =
        ArrayDeque<PathStep>().also {
            it.add(PathStep(start, 0, Direction.NONE))
        }
    distances[start] = 0

    while (queue.isNotEmpty()) {
        val (currentNode, currentDst) = queue.removeFirst()
        Direction
            .all()
            .map { currentNode + it.dx }
            .filter { it in this && this[it] != wall && currentDst + 1 < distances[it] }
            .forEach { p ->
                distances[p] = currentDst + 1
                queue.add(PathStep(p, currentDst + 1, Direction.NONE))
            }
    }

    return distances[end]
}
