package utils.map

class ColsAccess<T>(private val map: Map2D<T>): Iterable<Col<T>> {
    val size: Int get() = map.width
    operator fun get(x: Int) = Col(map, x)
    override fun iterator(): Iterator<Col<T>> = ColsIterator(map)
    fun listIterator(): ListIterator<Col<T>> = ColsIterator(map)

    operator fun get(range: IntRange): List<Col<T>> {
        return toList().subList(range.first, range.last)
    }
}

class Col<T>(private val map: Map2D<T>, private val x: Int): Iterable<T> {
    operator fun get(y: Int): T {
        return map[x,y]
    }

    override fun iterator(): Iterator<T> = ColIterator(map, x)

    operator fun get(range: IntRange): List<T> {
        return toList().subList(range.first, range.last)
    }
}

class ColsIterator<T>(private val map: Map2D<T>): ListIterator<Col<T>> {
    private var x = -1
    override fun hasNext(): Boolean = x < map.width - 1
    override fun hasPrevious(): Boolean = x > 0

    override fun next(): Col<T> {
        return map.cols[++x]
    }

    override fun previous(): Col<T> {
        return map.cols[--x]
    }

    override fun previousIndex(): Int  = x - 1
    override fun nextIndex(): Int = x + 1
}

class ColIterator<T>(private val map: Map2D<T>, private val x: Int): ListIterator<T> {
    private var y = -1

    override fun hasNext(): Boolean = y < map.height - 1
    override fun hasPrevious(): Boolean = y > 0

    override fun next() = map[x, ++y]
    override fun previous() = map[x, --y]

    override fun nextIndex() = y + 1
    override fun previousIndex() = y - 1
}
