package day01

import utils.readInput

fun part1(lines: List<String>): Int {
    val elves = readCalories(lines)

    return elves.maxOf { it.sum() }
}

fun part2(lines: List<String>): Int {
    val elves = readCalories(lines)

    return elves.map { it.sum() }.sorted().takeLast(3).sum()
}

fun readCalories(lines: List<String>): MutableList<List<Int>> {
    val elves: MutableList<List<Int>> = mutableListOf()
    var calories: MutableList<Int> = mutableListOf()

    lines.forEach { line ->
        if (line.isBlank()) {
            elves.add(calories)
            calories = mutableListOf()
        } else {
            calories.add(line.toInt())
        }
    }

    elves.add(calories)
    return elves
}

fun main() {
    val testInput = readInput("day01/test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("day01/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
