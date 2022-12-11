package day04

import utils.checkResult
import utils.readInput

fun parseInput(lines: List<String>): List<Pair<IntRange, IntRange>> {
    return lines.map { line ->
        val (e1, e2) = line.split(",").map { s ->
            val (start, end) = s.split("-").map { it.toInt() }
            IntRange(start, end)
        }
        Pair(e1,e2)
    }
}

fun IntRange.contains(range: IntRange): Boolean {
    return start <= range.first && last >= range.last
}

fun IntRange.overlaps(range: IntRange): Boolean {
    return range.first in this || range.last in this
}

fun part1(lines: List<String>): Int {
    val ranges = parseInput(lines).filter { it.first.contains(it.second) || it.second.contains(it.first) }

    return ranges.count()
}

fun part2(lines: List<String>): Int {
    val ranges = parseInput(lines).filter { it.first.overlaps(it.second) || it.second.overlaps(it.first) }

    return ranges.count()
}

fun main() {
    val testInput = readInput("day04/test")
    checkResult(part1(testInput), 2)
    checkResult(part2(testInput), 4)

    val input = readInput("day04/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
