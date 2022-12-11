package day11_2

class MonkeyGame(val lines: List<String>) {
    private val monkeys: List<Monkey>
    private val commonDivisor: Long

    init {
        val descriptions = lines.chunked(7)
        monkeys = List(descriptions.size) {
            Monkey.createFromDescription(descriptions[it])
        }

        commonDivisor = monkeys.map { it.test.divider }.reduce { a, b -> a * b}
    }

    fun nextRound() {
        monkeys.withIndex().forEach { (i, monkey) ->
            monkey.inspectItems(commonDivisor).forEach { (nextIndex, item) ->
                monkeys[nextIndex].receiveItem(item)
            }
        }
    }

    fun score(): Long {
        monkeys.withIndex().forEach { (i, monkey) ->
            println("Monkey $i inspected items ${monkey.score} times.")
        }

        val (m1,m2) = monkeys.sortedByDescending { it.score }.take(2).map { it.score }

        return m1 * m2
    }

    override fun toString(): String = buildString {
        monkeys.withIndex().forEach { (i, m) ->
            append("Monkey $i: ")
            appendLine(m.items.joinToString(", "))
        }
    }
}
