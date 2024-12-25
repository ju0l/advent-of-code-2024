package org.juol.aoc.y2024

import org.juol.aoc.utils.*

private data class Robot(
    var pos: Point,
    val velocity: Point,
    val w: Int,
    val h: Int,
) {
    fun move() {
        var (newX, newY) = pos + velocity
        if (newX < 0) {
            newX = (w + newX)
        } else if (newX >= w) {
            newX %= w
        }
        if (newY < 0) {
            newY = (h + newY)
        } else if (newY >= h) {
            newY %= h
        }
        pos = newX to newY
    }
}

private fun String.parse(
    w: Int,
    h: Int,
): List<Robot> =
    this.lines().map { row ->
        val (p1, p2) =
            row
                .split(" v=")[0]
                .split("=")[1]
                .split(",")
                .map { it.toInt() }
        val (v1, v2) = row.split("v=")[1].split(",").map { it.toInt() }

        Robot(p1 to p2, v1 to v2, w, h)
    }

private fun List<Robot>.count(
    w: Int,
    h: Int,
): Int {
    var q1 = 0
    var q2 = 0
    var q3 = 0
    var q4 = 0
    this.map { it.pos }.forEach { (x, y) ->
        if (x < w / 2 && y < h / 2) {
            q1++
        } else if (x > w / 2 && y < h / 2) {
            q2++
        } else if (x < w / 2 && y > h / 2) {
            q3++
        } else if (x > w / 2 && y > h / 2) {
            q4++
        }
    }
    return q1 * q2 * q3 * q4
}

private fun buildRoom(
    robots: List<Robot>,
    w: Int,
    h: Int,
): Grid<Int> {
    val room = filledGrid(w, h, 0)
    for (robot in robots) {
        room[robot.pos]++
    }
    return room
}

private fun part1(
    input: String,
    w: Int,
    h: Int,
): Int {
    val robots = input.parse(w, h)
    for (i in 0..<100) {
        robots.forEach { it.move() }
    }
    val sum = robots.count(w, h)
    return sum
}

private fun part2(
    input: String,
    w: Int,
    h: Int,
): Int {
    val robots = input.parse(w, h)

    for (i in 1..10000) {
        robots.forEach { it.move() }
        val room = buildRoom(robots, w, h)
        if (room.rows().any { row -> row.joinToString("") { if (it == 0) "." else "#" }.contains("#################") }) {
            return i
        }
    }
    return 0
}

fun main() {
    val testInput =
        """
        p=0,4 v=3,-3
        p=6,3 v=-1,-3
        p=10,3 v=-1,2
        p=2,0 v=2,-1
        p=0,0 v=1,3
        p=3,0 v=-2,-2
        p=7,6 v=-1,-3
        p=3,0 v=-1,-2
        p=9,3 v=2,3
        p=7,3 v=-1,2
        p=2,4 v=2,-3
        p=9,5 v=-3,-3
        """.trimIndent()

    val testAnswer1 = 12
    check(part1(testInput, 11, 7) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 0
    check(part2(testInput, 11, 7) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day14")
    // 214109808
    part1(input, 101, 103).println()
    // 7687
    part2(input, 101, 103).println()
}
