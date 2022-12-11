package day11_2

class Test(val divider: Long, private val trueIndex: Int, private val falseIndex: Int) {

    fun getNextIndex(item: Long): Int = if((item % divider).toInt() == 0) {
        trueIndex
    } else {
        falseIndex
    }

    companion object {
        fun createFromDescription(input: List<String>): Test {
            val divider = input[0].substring("  Test: divisible by ".length).toLong()
            val trueIndex = input[1].substring("    If true: throw to monkey ".length).toInt()
            val falseIndex = input[2].substring("    If false: throw to monkey ".length).toInt()

            return Test(divider, trueIndex, falseIndex)
        }
    }

    override fun toString(): String {
        return "divisible by $divider"
    }
}
