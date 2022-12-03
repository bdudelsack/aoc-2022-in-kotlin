import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/main/kotlin", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)


fun<T> checkResult(result: T, expected: T) = check(result == expected) { "Check failed. Expected '$expected' got '$result'." }

/**
 * Generic 2D Map Class
 */
class Map2D<T>(initData: List<List<T>>) {
    val data = initData.map { it.toMutableList() }.toMutableList()
    val height: Int get() = data.size
    val width: Int get() = data.first().size

    operator fun get(pt: Point) = this[pt.x, pt.y]
    operator fun get(x: Int, y: Int) = data[y][x]
    operator fun set(pt: Point, value: T) = value.also { this[pt.x,pt.y] = it }
    operator fun set(x: Int, y: Int, value: T) = value.also { data[y][x] = it }

    fun print() {
        for(y in 0 until height) {
            for(x in 0 until width) {
                print(get(x,y))
                print(" ")
            }
            println()
        }
    }

    fun<R> map(transform: (T, Int, Int) -> R): Map2D<R> {
        val data = data.mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                transform(c,x,y)
            }
        }

        return Map2D(data)
    }

    fun forEach(block: (T, Int, Int) -> Unit) {
        data.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                block(c,x,y)
            }
        }
    }

    companion object {
        fun<T> readFromLines(lines: List<String>, transform: (Char, Int, Int) -> T): Map2D<T> {
            val data = lines.mapIndexed { y,line -> line.mapIndexed { x, char -> transform(char,x,y) } }
            return Map2D(data)
        }

        fun<T> create(width: Int, height: Int, init: (x: Int, y: Int) -> T): Map2D<T> {
            val data = List(height) { y -> List(width) { x -> init(x,y) } }
            return Map2D(data)
        }
    }
}

data class Point(val x: Int, val y: Int) {

    companion object {
        fun fromString(input: String): Point {
            val (x, y) = input.trim().split(",").map { it.toInt() }
            return Point(x, y)
        }
    }
}

data class Line(val start: Point, val end: Point) {
    companion object {
        fun fromString(input: String): Line {
            val (start, end) = input.trim().split("->").map { Point.fromString(it) }
            return Line(start, end)
        }
    }
}
