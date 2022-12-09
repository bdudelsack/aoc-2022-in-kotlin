package utils.map

class RowsAccess<T>(private val map: Map2D<T>): Iterable<Row<T>> {
    val size: Int get() = map.height
    operator fun get(y: Int) = Row(map, y)
    override fun iterator(): Iterator<Row<T>> = RowsIterator(map)
    fun listIterator(): ListIterator<Row<T>> = RowsIterator(map)
}

class Row<T>(private val map: Map2D<T>, private val y: Int): Iterable<T> {
    operator fun get(x: Int): T {
        return map[x,y]
    }

    override fun iterator(): Iterator<T> = RowIterator(map, y)
}

class RowsIterator<T>(private val map: Map2D<T>): ListIterator<Row<T>> {
    private var y = -1
    override fun hasNext(): Boolean = y < map.height - 1
    override fun hasPrevious(): Boolean = y > 0

    override fun next(): Row<T> {
        return map.rows[++y]
    }

    override fun previous(): Row<T> {
        return map.rows[--y]
    }

    override fun previousIndex(): Int  = y - 1

    override fun nextIndex(): Int = y + 1
}

class RowIterator<T>(private val map: Map2D<T>, private val y: Int): ListIterator<T> {
    private var x = -1;

    override fun hasNext(): Boolean = x < map.width - 1
    override fun hasPrevious(): Boolean = x > 0

    override fun next() = map[++x, y]
    override fun previous() = map[--x, y]

    override fun nextIndex() = x + 1
    override fun previousIndex() = x - 1
}
