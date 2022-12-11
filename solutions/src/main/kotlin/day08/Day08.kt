package day08

import utils.map.Map2D
import utils.checkResult
import utils.readInput

fun part1(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines) { c, _, _ -> c }
    val visMap = Map2D.create(map.width, map.height) { x, y ->
        if(x == 0 || y == 0 || x == map.width - 1 || y == map.height -1 ) { 1 } else { 0 }
    }

    val rows = map.rows
    val cols = map.cols

    for(y in 1 until map.height - 1) {
        val row = rows[y]
        for(x in 1 until map.width - 1) {
            val l = row[0..x]
            val r = row[x + 1 .. row.size]
            val h = row[x]

            val visible = l.all { it < h } || r.all { it < h }

            visMap[x,y] = if(visible) 1 else 0
        }
    }

    for(x in 1 until map.width - 1) {
        val col = cols[x]
        for(y in 1 until map.height - 1) {
            if(visMap[x,y] == 0) {
                val t = col[0 .. y]
                val b = col[y + 1 .. col.size]
                val h = col[y]

                val visible = t.all { it < h } || b.all { it < h }
                visMap[x,y] = if(visible) 1 else 0
            }
        }
    }

    return visMap.sum()
}


fun part2(lines: List<String>): Int {
    val map = Map2D.readFromLines(lines) { c, _, _ -> c }


    fun score(x: Int, y: Int): Int {
        val row = map.rows[y]
        val col = map.cols[x]
        val h = map[x,y]

        // Right
        val r = if(x <= row.size) row[x + 1 .. row.size] else emptyList()
        val rIndex = r.indexOfFirst { it >= h }
        val rScore = if(rIndex == -1) r.size else rIndex + 1

        // Left
        val l = if(x <= row.size) row[0 .. x].reversed() else emptyList()
        val lIndex = l.indexOfFirst { it >= h }
        val lScore = if(lIndex == -1) l.size else lIndex + 1

        // Bottom
        val b = if(y <= col.size) col[y + 1 .. col.size] else emptyList()
        val bIndex = b.indexOfFirst { it >= h }
        val bScore = if(bIndex == -1) b.size else bIndex + 1

        // Top
        val t = if(y <= col.size) col[0 .. y].reversed() else emptyList()
        val tIndex = t.indexOfFirst { it >= h }
        val tScore = if(tIndex == - 1) t.size else tIndex + 1

        return tScore * lScore * rScore * bScore
    }


    return map.map { _, x, y, ->  score(x,y) }.max()
}

fun main() {
    val testInput = readInput("day08/test")
    checkResult(part1(testInput), 21)
    checkResult(part2(testInput), 8)

    val input = readInput("day08/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
