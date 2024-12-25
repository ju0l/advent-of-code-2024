package org.juol.aoc.y2024

import org.jgrapht.Graph
import org.jgrapht.alg.clique.PivotBronKerboschCliqueFinder
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.SimpleGraph
import org.juol.aoc.utils.*

private fun parse(input: String): Graph<String, DefaultEdge> =
    SimpleGraph<String, DefaultEdge>(DefaultEdge::class.java).also {
        input
            .lines()
            .map { it.split("-") }
            .forEach { (a, b) ->
                it.addVertex(a)
                it.addVertex(b)
                it.addEdge(a, b)
            }
    }

private fun combinations(
    int: List<String>,
    length: Int,
): List<List<String>> {
    val combinations = mutableListOf<List<String>>()

    fun generateCombinations(
        start: Int,
        current: List<String>,
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

private fun part1(input: String): Int {
    val graph = parse(input)
    val cliques =
        PivotBronKerboschCliqueFinder(graph)
            .asSequence()
            .filter { it.size >= 3 }
            .map { combinations(it.toList(), 3) }
            .flatten()
            .filter { it.any { c -> c.startsWith("t") } }
            .map { it.sorted() }
            .toSet()
    return cliques.size
}

private fun part2(input: String): String {
    val graph = parse(input)
    val p = PivotBronKerboschCliqueFinder(graph)
    val a =
        p.maxBy { it.size }.sorted()

    return a.joinToString(",")
}

fun main() {
    val testInput =
        """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
        """.trimIndent()

    val testAnswer1 = 7
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
    val testAnswer2 = "co,de,ka,ta"
    check(part2(testInput) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day23")
    // 1599
    part1(input).println()
    // av,ax,dg,di,dw,fa,ge,kh,ki,ot,qw,vz,yw
    part2(input).println()
}
