fun main() {
    fun calculateOutcome(player1: Char, player2: Char) = when(player1) {
        'A' -> {
            when(player2) {
                'X' -> { 3 }
                'Y' -> { 6 }
                'Z' -> { 0 }
                else -> throw Exception("Wrong input")
            }
        }
        'B' -> {
            when(player2) {
                'X' -> { 0 }
                'Y' -> { 3 }
                'Z' -> { 6 }
                else -> throw Exception("Wrong input")
            }
        }
        'C' -> {
            when(player2) {
                'X' -> { 6 }
                'Y' -> { 0 }
                'Z' -> { 3 }
                else -> throw Exception("Wrong input")
            }
        }
        else -> throw Exception("Wrong input")
    }

    fun chooseHand(player1: Char, outcome: Char): Char {
        return when(outcome) {
            'X' -> {
                when(player1) {
                    'A' -> { 'Z' }
                    'B' -> { 'X' }
                    'C' -> { 'Y' }
                    else -> throw Exception("Wrong input")
                }
            } // Loose
            'Y' -> player1 - 'A'.code + 'X'.code // Draw
            'Z' -> {
                when(player1) {
                    'A' -> { 'Y' }
                    'B' -> { 'Z' }
                    'C' -> { 'X' }
                    else -> throw Exception("Wrong input")
                }
            } // Win
            else -> throw Exception("Wrong input")
        }
    }

    fun part1(lines: List<String>): Int {
        val res = lines.map {
            val (p1, p2) = it.split(" ").map { s ->  s.first() }
            calculateOutcome(p1, p2) + 3 - ('Z' - p2)
        }

        return res.sum()
    }

    fun part2(lines: List<String>): Int {
        val res = lines.map {
            val (p1, outcome) = it.split(" ").map { s ->  s.first() }
            val p2 = chooseHand(p1, outcome)
            calculateOutcome(p1, p2) + 3 - ('Z' - p2)
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
