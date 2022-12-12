package day12_2

import utils.Point
import utils.checkResult
import utils.map.Map2D
import utils.readInput

class Day12_2Solver(heightMap: Map2D<Char>) {
    private val startPoint = heightMap.findPoint { it == 'E' }
    var solution: Pair<Point, Int>? = null

    val map = heightMap.map { c,_,_ ->
        when(c) {
            'S' -> { 0 }
            'E' -> { 25 }
            else -> { c - 'a'}
        }
    }

    val waypoints = Map2D.create<Waypoint?>(heightMap.width, heightMap.height) { _, _, -> null }
    var queue = listOf(startPoint)

    init {
        waypoints[startPoint] = Waypoint(0, null)
    }

    fun solve() {
        while(queue.isNotEmpty()) {
            step()
        }
    }

    fun step() {
        queue = buildList {
            queue.forEach { from ->
                val directions = listOf(
                    Point(1, 0),    // Right
                    Point(-1, 0),   // Left
                    Point(0, -1),   // Top
                    Point(0, 1)     // Bottom
                ).map { from + it }.filter { to -> canWalk(from, to) }

                val targetWeight = waypoints[from]!!.weight + 1

                directions.forEach { to ->
                    if(map[to] == 0) {
                        queue = emptyList()
                        solution = Pair(to, targetWeight)
                        return
                    }

                    if(waypoints[to] == null || waypoints[to]!!.weight > targetWeight) {
                        waypoints[to] = Waypoint(targetWeight, from)
                        add(to)
                    }
                }
            }
        }
    }

    private fun canWalk(from: Point, to: Point): Boolean {
        if(to.x < 0 || to.x >= map.width || to.y < 0 || to.y >= map.height) {
            return false
        }

        return map[from] - 1 <= map[to]
    }

    data class Waypoint(val weight: Int, val prev: Point?) {
        override fun toString(): String = weight.toString()
    }
}

fun part2(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines) { c, _, _ -> c }
    val solver = Day12_2Solver(map)

    solver.solve()

    return solver.solution!!.second
}


fun main() {
    val testInput = readInput("day12/test")
    checkResult(part2(testInput), 29)

    val input = readInput("day12/input")
    println("Part 2: ${part2(input)}")
}
