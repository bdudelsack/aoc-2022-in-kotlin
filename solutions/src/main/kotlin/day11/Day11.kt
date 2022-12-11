package day11

import utils.checkResult
import utils.readInput

fun part1(lines: List<String>): Int {
    val game = MonkeyGame(lines)

    println(game)

    repeat(20) {
        game.nextRound()

        println()
        println(game)
    }

    return game.score()
}


fun main() {
    val testInput = readInput("day11/test")
    checkResult(part1(testInput), 10605)

    val input = readInput("day11/input")
    println("Part 1: ${part1(input)}")
}
