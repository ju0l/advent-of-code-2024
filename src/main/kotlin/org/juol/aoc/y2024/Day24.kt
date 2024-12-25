package org.juol.aoc.y2024

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout
import com.mxgraph.layout.mxIGraphLayout
import com.mxgraph.util.mxCellRenderer
import org.jgrapht.ext.JGraphXAdapter
import org.jgrapht.graph.DefaultEdge
import org.jgrapht.graph.DirectedAcyclicGraph
import org.juol.aoc.utils.println
import org.juol.aoc.utils.readInput
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

private data class Gate(
    val a: String,
    val b: String,
    val op: String,
    val out: String?,
) {
    operator fun invoke(
        x: Boolean,
        y: Boolean,
    ): Boolean =
        when (op) {
            "AND" -> x and y
            "OR" -> x or y
            "XOR" -> x xor y
            else -> error("Unknown op: $op")
        }
}

private fun parse(input: String): Pair<Map<String, Boolean>, List<Gate>> {
    val (init, ops) = input.split("\n\n")
    val initMap =
        init
            .lines()
            .associate {
                val (key, value) = it.split(": ")
                key to (value == "1")
            }
    val gateList =
        ops.lines().map {
            val (a, op, b, _, out) = it.split(" ")
            Gate(a, b, op, out)
        }

    return initMap to gateList
}

private fun part1(input: String): Long {
    val (initMap, gates) = parse(input)

    val zOps = gates.filter { it.out?.startsWith("z") == true }

    fun findValue(gate: Gate): Boolean {
        val a = initMap[gate.a] ?: findValue(gates.find { it.out == gate.a }!!)
        val b = initMap[gate.b] ?: findValue(gates.find { it.out == gate.b }!!)

        return gate(a, b)
    }

    val x =
        zOps
            .sortedByDescending { it.out }
            .map { findValue(it) }
            .map { if (it) 1 else 0 }
            .joinToString("")
            .toLong(2)

    return x
}

private fun findOut(
    gates: List<Gate>,
    x: String?,
    op: String,
    y: String?,
): String? =
    gates
        .firstOrNull {
            (it.a == x && it.op == op && it.b == y) ||
                (it.a == y && it.op == op && it.b == x)
        }?.out

private fun swap(
    rules: List<Gate>,
    out1: String?,
    out2: String?,
): List<String?> {
    val newRules =
        rules.map { rule ->
            when (rule.out) {
                out1 -> rule.copy(out = out2)
                out2 -> rule.copy(out = out1)
                else -> rule
            }
        }
    return fix(newRules) + listOf(out1, out2)
}

private fun fix(rules: List<Gate>): List<String?> {
    var cin = findOut(rules, "x00", "AND", "y00")
    for (i in 1 until 45) {
        val x = "x${i.toString().padStart(2, '0')}"
        val y = "y${i.toString().padStart(2, '0')}"
        val z = "z${i.toString().padStart(2, '0')}"

        val xor1 = findOut(rules, x, "XOR", y)
        val and1 = findOut(rules, x, "AND", y)
        val xor2 = findOut(rules, cin, "XOR", xor1)
        val and2 = findOut(rules, cin, "AND", xor1)

        if (xor2 == null && and2 == null) {
            return swap(rules, xor1, and1)
        }

        val carry = findOut(rules, and1, "OR", and2)
        if (xor2 != z) {
            return swap(rules, z, xor2)
        } else {
            cin = carry
        }
    }
    return emptyList()
}

private fun printGraph(gates: List<Gate>) {
    val g =
        DirectedAcyclicGraph<String, DefaultEdge>(DefaultEdge::class.java).also {
            val xs =
                gates.filter {
                    it.a.startsWith("x") ||
                        it.b.startsWith("x") ||
                        it.a.startsWith("y") ||
                        it.b.startsWith("y")
                }
            val zs =
                gates.filter {
                    it.a.startsWith("z") ||
                        it.b.startsWith("z")
                }
            val other =
                gates.filter {
                    !(
                        it.a.startsWith("x") ||
                            it.b.startsWith("x") ||
                            it.a.startsWith("y") ||
                            it.b.startsWith("y") ||
                            it.a.startsWith("z") ||
                            it.b.startsWith("z")
                    )
                }

            (xs + other + zs).forEach { rule ->
                val (a, b) = listOf(rule.a, rule.b).sorted()
                it.addVertex(a)
                it.addVertex(b)
                val z = "$a ${rule.op} $b"
                it.addVertex(z)
                it.addVertex(rule.out)
                it.addEdge(a, z)
                it.addEdge(b, z)
                it.addEdge(z, rule.out)
            }
        }

    val graphAdapter = JGraphXAdapter(g)
    val layout: mxIGraphLayout = mxHierarchicalLayout(graphAdapter)
    layout.execute(graphAdapter.getDefaultParent())
    val image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2.0, Color.WHITE, true, null)
    val imgFile = File("src/main/resources/graph.png")
    ImageIO.write(image, "PNG", imgFile)
}

private fun part2(input: String): String {
    val (_, gates) = parse(input)
//    printGraph(gates)
    val swaps = fix(gates)
    val s = swaps.filterNotNull().sorted().joinToString(",")
    return s
}

fun main() {
    val testInput =
        """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1
        
        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
        """.trimIndent()

    val testInput2 =
        """
        x00: 0
        x01: 1
        x02: 0
        x03: 1
        x04: 0
        x05: 1
        y00: 0
        y01: 0
        y02: 1
        y03: 1
        y04: 0
        y05: 1

        x00 AND y00 -> z05
        x01 AND y01 -> z02
        x02 AND y02 -> z01
        x03 AND y03 -> z03
        x04 AND y04 -> z04
        x05 AND y05 -> z00
        """.trimIndent()

    val testAnswer1 = 2024L
    check(part1(testInput) == testAnswer1) { "answer 1 to test is wrong" }
//    val testAnswer2 = "z00,z01,z02,z05"
//    check(part2(testInput2) == testAnswer2) { "answer 2 to test is wrong" }

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}
