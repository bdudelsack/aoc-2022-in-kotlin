package day11_2

import utils.checkResult
import utils.readInput

fun part2(lines: List<String>): Long {
    val game = MonkeyGame(lines)


    repeat(10000) {
        game.nextRound()
    }

    return game.score()
}


fun main() {
    val testInput = readInput("day11/test")
    checkResult(part2(testInput), 2713310158L)

    val input = readInput("day11/input")
    println("Part 1: ${part2(input)}")
}
