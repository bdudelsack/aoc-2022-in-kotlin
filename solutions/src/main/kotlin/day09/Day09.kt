package day09

import utils.Point
import utils.checkResult
import utils.readInput
import kotlin.math.sign

fun readCommands(lines: List<String>): List<Char> = buildList {
    lines.forEach { line ->
        val (c, count) = line.split(" ")
        repeat(count.toInt()) { add(c.first()) }
    }
}

fun part1(lines: List<String>): Int {
    val commands = readCommands(lines)

    val positions = mutableSetOf<Point>()
    var H = Point(0,0)
    var T = H
    val s = H

    fun printGrid(minX: Int, minY: Int, maxX: Int, maxY: Int) {
        for(y in minY .. maxY) {
            for(x in minX .. maxX) {
                print(when(Point(x,y)) {
                    H -> 'H'
                    T -> 'T'
                    s -> 's'
                    else -> '.'
                })
            }
        }
    }

    commands.forEach { command ->
        // Move Head
        when(command) {
            'R' -> { H += Point(1, 0) }
            'L' -> { H += Point(-1, 0) }
            'U' -> { H += Point(0, -1) }
            'D' -> { H += Point(0, 1) }
        }

        val dist = H.dist(T)
        val vec = H - T
        if(dist.x > 1 || dist.y > 1) {
            if(dist.x > 0 && dist.y == 0) {
                T += Point(vec.x / 2, 0)
            } else if(dist.y > 0 && dist.x == 0) {
                T += Point(0, vec.y / 2)
            } else {
                T += Point(vec.x.sign, vec.y.sign)
            }
        }

        positions.add(T)
    }


    return positions.size
}

fun part2(lines: List<String>): Int {
    val commands = readCommands(lines)

    val positions = mutableSetOf<Point>()
    val rope = MutableList(10) { Point(0,0) }

    fun printGrid(minX: Int, minY: Int, maxX: Int, maxY: Int) {
        val tail = rope.subList(1,9)
        for(y in minY .. maxY) {
            for(x in minX .. maxX) {
                print(when {
                    Point(x,y) == rope[0] -> 'H'
                    tail.contains(Point(x,y)) -> tail.indexOfFirst { it == Point(x,y) } + 1
                    else -> '.'
                })
            }
            println()
        }
        println()
    }

//    printGrid(0,-5,6,0)

    commands.forEach { command ->
        when (command) {
            'R' -> {
                rope[0] += Point(1, 0)
            }

            'L' -> {
                rope[0] += Point(-1, 0)
            }

            'U' -> {
                rope[0] += Point(0, -1)
            }

            'D' -> {
                rope[0] += Point(0, 1)
            }
        }

        for(i in 1..9) {
            val dist = rope[i-1].dist(rope[i])
            val vec = rope[i-1] - rope[i]
            if(dist.x > 1 || dist.y > 1) {
                if(dist.x > 0 && dist.y == 0) {
                    rope[i] += Point(vec.x / 2, 0)
                } else if(dist.y > 0 && dist.x == 0) {
                    rope[i] += Point(0, vec.y / 2)
                } else {
                    rope[i] += Point(vec.x.sign, vec.y.sign)
                }
            }
        }

//        printGrid(0,-5,6,0)
        positions.add(rope[9])
    }


    return positions.size
}

fun main() {
    val testInput = readInput("day09/test")
    checkResult(part1(testInput), 13)
    checkResult(part2(testInput), 1)

    val largeTestInput = readInput("day09/test_large")
    checkResult(part2(largeTestInput), 36)

    val input = readInput("day09/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
