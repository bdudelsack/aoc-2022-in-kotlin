@JvmInline
value class Item(private val char: Char) {
    init {
        require(char in 'a'..'z' || char in 'A'..'Z' )
    }

    fun priority() = if(char.isLowerCase()) char - 'a' + 1 else char - 'A' + 27
}

fun main() {
    fun part1(lines: List<String>): Int {
        val res = lines.map {
            val (c1, c2) = it.chunked(it.length / 2).map { s ->
                s.toCharArray().map { c -> Item(c) }
            }

            c1.intersect(c2.toSet())
        }.map { it.sumOf { item -> item.priority() } }

        return res.sum()
    }

    fun part2(lines: List<String>): Int {
        val res = lines.chunked(3).map {
            val (p1,p2,p3) = it.map { s -> s.toCharArray().map { c -> Item(c) }.toSet() }
            p1.intersect(p2).intersect(p3).first()
        }

        return res.sumOf { it.priority() }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
