package org.juol.aoc.day15

import org.juol.aoc.utils.*
import org.juol.aoc.utils.Direction.*
import org.juol.aoc.utils.Orientation.VERTICAL

private fun Char.toDirection(): Direction =
    when (this) {
        '^' -> UP
        'v' -> DOWN
        '<' -> LEFT
        '>' -> RIGHT
        else -> error("Unknown move: $this")
    }

private fun parse(
    matrixInput: String,
    movesInput: String,
): Pair<Grid<String>, List<Direction>> {
    val matrix = matrixInput.toStringGrid()
    val moves = movesInput.toCharArray().filter { it != '\n' }.map { it.toDirection() }

    return matrix to moves
}

fun extend(input: String): String =
    input
        .lines()
        .joinToString("\n") { row ->
            row.toCharArray().joinToString("") { c ->
                when (c) {
                    'O' -> "[]"
                    '@' -> "@."
                    else -> "$c$c"
                }
            }
        }

private fun part1(input: String): Long {
    val (matrixInput, movesInput) = input.split("\n\n")
    val (matrix, moves) = parse(matrixInput, movesInput)

    fun move(
        p: Point,
        move: Direction,
    ) {
        val np = p + move.dx
        if (matrix[np] == "O") {
            move(np, move)
        }
        if (matrix[np] != ".") {
            return
        }
        matrix[np] = matrix[p]
        matrix[p] = "."
    }

    moves.forEach {
        val robotPosition = matrix.indexOf("@") ?: error("Robot not found")
        move(robotPosition, it)
    }

    val sum =
        matrix.sumOf { x, y, s ->
            if (s == "O") x + (y * 100L) else 0
        }

    return sum
}

private fun part2(input: String): Long {
    val (matrixInput, movesInput) = input.split("\n\n")
    val (matrix, moves) = parse(extend(matrixInput), movesInput)

    fun canMove(
        boxes: List<Pair<Point, Point>>,
        move: Direction,
    ): Boolean =
        boxes.all { (lp, rp) ->
            val nlp = lp + move.dx
            val nrp = rp + move.dx
            val nlc = matrix[nlp]
            val nrc = matrix[nrp]

            if (nlc == "#" || nrc == "#") {
                return@all false
            }
            if (move == LEFT) {
                if (nlc == ".") {
                    return@all true
                }
                return@all canMove(listOf((nlp + (-1 to 0)) to nlp), move)
            }
            if (move == RIGHT) {
                if (nrc == ".") {
                    return@all true
                }
                return@all canMove(listOf(nrp to (nrp + (1 to 0))), move)
            }
            if (nlc == "." && nrc == ".") {
                return@all true
            }
            if (nlc == "[" && nrc == "]") {
                return@all canMove(listOf(nlp to nrp), move)
            }
            if (nlc == "]" && nrc == "[") {
                return@all canMove(
                    listOf(
                        (nlp + (-1 to 0)) to nlp,
                        nrp to (nrp + (1 to 0)),
                    ),
                    move,
                )
            }
            if (nlc == "]") {
                return@all canMove(listOf((nlp + (-1 to 0)) to nlp), move)
            }
            if (nrc == "[") {
                return@all canMove(listOf(nrp to (nrp + (1 to 0))), move)
            }
            false
        }

    fun moveBox(
        boxes: List<Pair<Point, Point>>,
        move: Direction,
    ) {
        if (!canMove(boxes, move)) {
            return
        }

        boxes.forEach { (lp, rp) ->
            val nlp = lp + move.dx
            val nrp = rp + move.dx
            val nlc = matrix[nlp]
            val nrc = matrix[nrp]

            if (nlc == "[" && nrc == "]") {
                moveBox(listOf(nlp to nrp), move)
            } else if (nlc == "]" && nrc == "[" && move.orientation == VERTICAL) {
                moveBox(
                    listOf(
                        (nlp + (-1 to 0)) to nlp,
                        nrp to (nrp + (1 to 0)),
                    ),
                    move,
                )
            } else {
                if (nlc == "]" && move != RIGHT) {
                    moveBox(listOf((nlp + (-1 to 0)) to nlp), move)
                }
                if (nrc == "[" && move != LEFT) {
                    moveBox(listOf(nrp to (nrp + (1 to 0))), move)
                }
            }
        }

        boxes.forEach { (lp, rp) ->
            val nlp = lp + move.dx
            val nrp = rp + move.dx
            val lc = matrix[lp]
            val rc = matrix[rp]
            matrix[lp] = "."
            matrix[rp] = "."
            matrix[nlp] = lc
            matrix[nrp] = rc
        }
    }

    fun move(
        p: Point,
        move: Direction,
    ) {
        val np = p + move.dx
        if (matrix[np] == "[") {
            moveBox(listOf(np to (np + (1 to 0))), move)
        }
        if (matrix[np] == "]") {
            moveBox(listOf((np + (-1 to 0)) to np), move)
        }
        if (matrix[np] == ".") {
            matrix[np] = "@"
            matrix[p] = "."
        }
    }

    moves.forEachIndexed { i, d ->
        val robotPosition = matrix.indexOf("@") ?: error("Robot not found")
        move(robotPosition, d)
    }

    val sum =
        matrix.sumOf { x, y, s ->
            if (s == "[") x + (y * 100L) else 0
        }
    return sum
}

fun main() {
    val testInput =
        """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent()

    val testInput2 =
        """
        ##############
        ##......##..##
        ##..........##
        ##....[][]@.##
        ##....[]....##
        ##..........##
        ##############

        <vv<<^^<<^^
        """.trimIndent()

    val testAnswer1 = 10092L
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = 9021L
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day15")
    // 1511865
    part1(input).println()
    // 1519991
    part2(input).println()
}
