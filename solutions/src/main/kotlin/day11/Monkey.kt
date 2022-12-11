package day11

data class Monkey(private val startItems: Iterable<Int>, val operation: Operation, val test: Test) {
    val items: MutableList<Int> = startItems.toMutableList()
    var score: Int = 0

    fun inspectItems(): List<Pair<Int, Int>> {
        score += items.size

        val res = items.map {
            var res = operation.execute(it)

            println("  Monkey inspects an item with a worry level of $it.")
            println("    Worry level after inspection ($operation): $res")

            res /= 3

            println("    Monkey gets bored with item. Worry level is divided by 3 to $res")

            val nextIndex = test.getNextIndex(res)

            println("    Item with worry level $res is thrown to monkey $nextIndex.")

            Pair(nextIndex, res)
        }

        items.clear()

        return res
    }

    fun receiveItem(item: Int) {
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

        private fun parseItems(line: String): List<Int> {
            return line.substring("  Starting items: ".length).split(", ").map { it.toInt() }
        }
    }
}
