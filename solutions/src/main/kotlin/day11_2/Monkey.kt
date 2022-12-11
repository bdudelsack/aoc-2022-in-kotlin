package day11_2

data class Monkey(private val startItems: Iterable<Long>, val operation: Operation, val test: Test) {
    val items: MutableList<Long> = startItems.toMutableList()
    var score: Long = 0

    fun inspectItems(commonDivisor: Long): List<Pair<Int, Long>> {
        score += items.size

        val res = items.map {
            var res = operation.execute(it)

            res %= commonDivisor

            val nextIndex = test.getNextIndex(res)

            Pair(nextIndex, res)
        }

        items.clear()

        return res
    }

    fun receiveItem(item: Long) {
        items.add(item)
    }

    companion object {
        fun createFromDescription(lines: List<String>): Monkey {
            return Monkey(
                parseItems(lines[1]),
                Operation.createFromDescription(lines[2]),
                Test.createFromDescription(lines.subList(3,6))
            )
        }

        private fun parseItems(line: String): List<Long> {
            return line.substring("  Starting items: ".length).split(", ").map { it.toLong() }
        }
    }
}
