/**
 * Generic 2D Map Class
 */
class Map2D<T>(initData: List<List<T>>): Iterable<T> {
    private val data = initData.map { it.toMutableList() }.toMutableList()
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

    fun rows() = RowsAccess(this)
    fun cols() = ColsAccess(this)

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

    override fun iterator(): Iterator<T> = Map2DIterator(this)
}

class Map2DIterator<T>(private val map: Map2D<T>): Iterator<T> {
    private var x: Int = 0
    private var y: Int = 0

    override fun hasNext(): Boolean {
        return x < map.width && y < map.height
    }

    override fun next(): T {
        val c: T = map[x,y]
        if(++x == map.width) {
            x = 0
            y++
        }
        return c
    }
}

class RowsAccess<T>(private val map: Map2D<T>) {
    val size: Int get() = map.height
    operator fun get(y: Int) = buildList {
        for(x in 0 until map.width) {
            add(map[x,y])
        }
    }
}

class ColsAccess<T>(private val map: Map2D<T>) {
    val size: Int get() = map.width
    operator fun get(x: Int) = buildList {
        for(y in 0 until map.height) {
            add(map[x,y])
        }
    }
}
