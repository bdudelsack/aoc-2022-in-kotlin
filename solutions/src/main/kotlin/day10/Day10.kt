package day10

import utils.checkResult
import utils.readInput
import kotlin.math.abs

class Device(lines: List<String>) {
    val listX = mutableListOf(1)

    init {
        lines.forEach { exec(it) }
    }

    private fun exec(line: String) {
        if (line == "noop") {
            listX.add(listX.last())
        } else {
            val (_, param) = line.split(" ")
            listX.add(listX.last())
            listX.add(listX.last() + param.toInt())
        }
    }

    fun signalStrength(): Int {
        val values = buildList {
            for (i in 20 until listX.size step 40) {
                add(Pair(i, listX[i - 1]))
            }
        }

        return values.sumOf { it.first * it.second }
    }

    fun display() {
        for((i,b) in bits().withIndex()) {
            if(i % 40 == 0) println()
            print(if(b) "#" else ".")
        }
    }

    fun bits() = buildList {
        for (c in 0 until 40 * 6) {
            val x = c % 40
            add(abs(listX[c] - x) <= 1)
        }
    }
}

fun part1(lines: List<String>): Int {
    val device = Device(lines)
    return device.signalStrength()
}

fun part2(lines: List<String>): Int {
    val device = Device(lines)
    device.display()

    return 0
}

fun main() {
    val testInput = readInput("day10/test")
    checkResult(part1(testInput), 13140)
    checkResult(part2(testInput), 0)

    val input = readInput("day10/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
