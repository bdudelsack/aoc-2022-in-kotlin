package day05

import checkResult
import readInput

lateinit var stacks: ArrayList<List<Char>>
lateinit var commands: List<MoveCommand>

data class MoveCommand(val count: Int, val from: Int, val to: Int) {
    companion object {
        fun parse(input: String): MoveCommand {
            val regex = "move ([0-9]+) from ([0-9])+ to ([0-9]+)".toRegex()
            val match = regex.matchEntire(input) ?: throw Exception("Cannot parse command: $input")

            return MoveCommand(
                match.groupValues[1].toInt(),
                match.groupValues[2].toInt() - 1,
                match.groupValues[3].toInt() - 1,
            )
        }
    }
}

fun parseInput(lines: List<String>) {
    val cursor = lines.indexOfFirst { it.startsWith(" 1") }
    val size = lines[cursor].last().digitToInt()

    stacks = ArrayList(List(size) { listOf() })

    lines.subList(0, cursor).forEach {
        it.chunked(4).map { s -> s.trim() }.forEachIndexed { i,v ->
            if(v.isNotBlank()) {
                stacks[i] = stacks[i] + v[1]
            }
        }
    }

    commands = lines.subList(cursor + 2, lines.size).map { MoveCommand.parse(it) }
}

fun executeCommands(reverse: Boolean = true) {
    commands.forEach {
        val movedCrates = stacks[it.from].subList(0, it.count)
        stacks[it.from] = stacks[it.from].subList(it.count, stacks[it.from].size)
        stacks[it.to] = buildList {
            addAll(if(reverse) movedCrates.reversed() else movedCrates)
            addAll(stacks[it.to])
        }
    }
}

fun part1(lines: List<String>): String {
    parseInput(lines)
    executeCommands()

    return stacks.joinToString("") { it.first().toString() }
}

fun part2(lines: List<String>): String {
    parseInput(lines)
    executeCommands(false)

    return stacks.joinToString("") { it.first().toString() }
}

fun main() {
    val testInput = readInput("day05/test")
    checkResult(part1(testInput), "CMZ")
    checkResult(part2(testInput), "MCD")

    val input = readInput("day05/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
