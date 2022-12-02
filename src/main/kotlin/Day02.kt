enum class PlayerOne(val hand: Char) {
    ROCK('A'),
    PAPER('B'),
    SCISSORS('C');

    companion object {
        infix fun of(hand: Char) = PlayerOne.values().first { it.hand == hand }
    }
}

enum class PlayerTwo(val hand: Char) {
    ROCK('X'),
    PAPER('Y'),
    SCISSORS('Z');

    companion object {
        infix fun of(hand: Char) = PlayerTwo.values().first { it.hand == hand }
    }

    fun score() = ordinal + 1
}

enum class Outcome(val outcome: Char, val score: Int) {
    LOOSE('X', 0),
    DRAW('Y', 3),
    WIN('Z', 6);

    companion object {
        infix fun of(outcome: Char) = Outcome.values().first { it.outcome == outcome }
    }
}

val table = mapOf(
    PlayerOne.ROCK to PlayerTwo.ROCK to Outcome.DRAW,
    PlayerOne.PAPER to PlayerTwo.PAPER to Outcome.DRAW,
    PlayerOne.SCISSORS to PlayerTwo.SCISSORS to Outcome.DRAW,

    PlayerOne.ROCK to PlayerTwo.PAPER to Outcome.WIN,
    PlayerOne.PAPER to PlayerTwo.SCISSORS to Outcome.WIN,
    PlayerOne.SCISSORS to PlayerTwo.ROCK to Outcome.WIN,

    PlayerOne.PAPER to PlayerTwo.ROCK to Outcome.LOOSE,
    PlayerOne.SCISSORS to PlayerTwo.PAPER to Outcome.LOOSE,
    PlayerOne.ROCK to PlayerTwo.SCISSORS to Outcome.LOOSE,
)

fun main() {
    fun calculateOutcome(p1: PlayerOne, p2: PlayerTwo): Int {
        val res =  table[p1 to p2]
        return res?.score?.let { it + p2.score() } ?: throw Exception("Invalid input")
    }

    fun chooseHand(player1: PlayerOne, outcome: Outcome): PlayerTwo {
        return table.entries.first {
            it.value == outcome && it.key.first == player1
        }.key.second
    }

    fun part1(lines: List<String>): Int {
        val res = lines.map {
            val (p1, p2) = it.split(" ").map { s ->  s.first() }
            calculateOutcome(PlayerOne of p1, PlayerTwo of p2)
        }

        return res.sum()
    }

    fun part2(lines: List<String>): Int {
        val res = lines.map {
            val (p1, outcome) = it.split(" ").map { s ->  s.first() }
            val p2 = chooseHand(PlayerOne of p1, Outcome of outcome)
            calculateOutcome(PlayerOne of p1, p2)
        }

        return res.sum()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
