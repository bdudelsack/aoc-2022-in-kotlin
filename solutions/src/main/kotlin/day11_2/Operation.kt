package day11_2

class Operation(private val left: String, private val operation: Char, private val right: String) {

    fun execute(item: Long): Long {
        val operand = if(right == "old") item else right.toLong()

        return when(operation) {
            '+' -> { item + operand }
            '-' -> { item - operand }
            '*' -> { item * operand }
            '/' -> { item / operand }

            else -> throw Exception("Unknown operation: $operation")
        }
    }

    companion object {
        fun createFromDescription(input: String): Operation {
            val (l,o,r) = input.substring("  Operation: new = ".length).split(" ").map { it.trim() }

            return Operation(l, o.first(), r)
        }
    }

    override fun toString(): String {
        return "new = $left $operation $right"
    }
}
