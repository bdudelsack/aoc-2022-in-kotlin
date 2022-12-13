package day13

import utils.checkResult
import utils.readInput

sealed class PacketData : Comparable<PacketData> {
    data class Integer(val value: Int) : PacketData() {
        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is Integer -> {
                    if (value == other.value) {
                        0
                    } else if (value <= other.value) {
                        -1
                    } else {
                        1
                    }
                }

                is Array -> {
                    val convertedLeft = Array(listOf(this))
                    convertedLeft.compareTo(other)
                }
            }
        }

        override fun toString(): String {
            return value.toString()
        }
    }

    data class Array(val items: List<PacketData>) : PacketData() {
        override fun compareTo(other: PacketData): Int {
            return when (other) {
                is Integer -> {
                    val convertedRight = Array(listOf(other))
                    compareTo(convertedRight)
                }

                is Array -> {
                    val res: Int

                    val leftDeque = ArrayDeque(items)
                    val rightDeque = ArrayDeque(other.items)

                    while (true) {
                        when {
                            leftDeque.isEmpty() && rightDeque.isEmpty() -> {
                                res = 0;
                                break
                            }

                            leftDeque.isEmpty() -> {
                                res = -1;
                                break
                            }

                            rightDeque.isEmpty() -> {
                                res = 1;
                                break
                            }

                            else -> {
                                val l = leftDeque.removeFirst()
                                val r = rightDeque.removeFirst()

                                val c = l.compareTo(r)

                                if (c != 0) {
                                    res = c
                                    break
                                }
                            }
                        }
                    }

                    res
                }
            }
        }

        override fun toString(): String {
            return "[${items.joinToString(",")}]"
        }
    }

    companion object {
        fun parseString(str: String): PacketData {
            if (str.first() == '[' && str.last() == ']') {
                return parseArray(str.substring(1 until str.length - 1))
            }

            return Integer(str.toInt())
        }

        private fun parseArray(str: String): Array {
            var nested = 0
            var lastIndex = 0
            val items = buildList {
                str.toCharArray().forEachIndexed { i, c ->
                    when (c) {
                        '[' -> {
                            nested++
                        }

                        ']' -> {
                            nested--
                        }

                        ',' -> {
                            if (nested == 0) {
                                add(parseString(str.substring(lastIndex, i)))
                                lastIndex = i + 1
                            }
                        }
                    }
                }

                val lastSubstring = str.substring(lastIndex, str.length)
                if (lastSubstring.isNotBlank()) {
                    add(parseString(lastSubstring))
                }
            }

            return Array(items)
        }
    }

}

fun part1(lines: List<String>): Int {
    val input = lines.chunked(3).map {
        PacketData.parseString(it[0]) to PacketData.parseString(it[1])
    }

    val res = buildList {
        input.forEachIndexed { i, p ->
            if (p.first.compareTo(p.second) == -1) {
                add(i + 1)
            }
        }
    }

    return res.sum()
}

fun part2(lines: List<String>): Int {
    val p1 = PacketData.Array(listOf(PacketData.Array(listOf(PacketData.Integer(2)))))
    val p2 = PacketData.Array(listOf(PacketData.Array(listOf(PacketData.Integer(6)))))

    val packets = lines.filter { it.isNotBlank() }.map { PacketData.parseString(it) }.union(
        listOf(p1, p2)
    ).sorted()

    val i1 = packets.indexOf(p1) + 1
    val i2 = packets.indexOf(p2) + 1

    return i1 * i2
}

fun main() {
    val testInput = readInput("day13/test")
    checkResult(part1(testInput), 13)
    checkResult(part2(testInput), 140)

    val input = readInput("day13/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
