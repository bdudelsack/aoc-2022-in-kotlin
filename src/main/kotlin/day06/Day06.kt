package day06

import checkResult
import readInput

fun part1(line: String, size: Int = 4): Int {
    val res = line.toCharArray().asSequence().windowed(size).withIndex().first {
        it.value.toSet().size == size
    }

    return res.index + size
}


fun main() {
    val testInput = readInput("day06/test")
    checkResult(part1(testInput.first(), 4), 7)
    checkResult(part1(testInput.first(), 14), 19)

    val input = readInput("day06/input")
    println("Part 1: ${part1(input.first(), 4)}")
    println("Part 2: ${part1(input.first(), 14)}")
}
