package day11

class MonkeyGame(val lines: List<String>) {
    val monkeys: List<Monkey>

    init {
        val descriptions = lines.chunked(7)
        monkeys = List(descriptions.size) {
            Monkey.createFromDescription(descriptions[it])
        }
    }

    fun nextRound() {
        monkeys.withIndex().forEach { (i, monkey) ->
            println("Monkey $i: ")
            monkey.inspectItems().forEach { (nextIndex, item) ->
                monkeys[nextIndex].receiveItem(item)
            }
        }
    }

    fun score(): Int {
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
