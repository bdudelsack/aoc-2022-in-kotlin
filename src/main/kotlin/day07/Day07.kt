package day07

import checkResult
import readInput

data class FSNode(
    val name: String = "/",
    val type: Type = Type.DIRECTORY,
    var size: Long = 0L,
    val children: MutableList<FSNode> = mutableListOf(),
    val parent: FSNode? = null
) {
    enum class Type {
        DIRECTORY,
        FILE
    }

    fun addChildren(nodes: Iterable<FSNode>) {
        children.addAll(nodes)
    }

    fun display() {
        println(dump())
    }

    private fun dump(ident: Int = 0): String {
        return buildString {
            append(" ".repeat(ident * 2))
            append(" - ")
            appendLine("$name ($type, size=$size)")

            children.forEach { child ->
                append(child.dump(ident + 1))
            }
        }
    }

    fun recursiveFind(predicate: (FSNode) -> Boolean): List<FSNode> = buildList {
        if(predicate(this@FSNode)) {
            add(this@FSNode)
        }

        children.forEach { child: FSNode ->
            addAll(child.recursiveFind(predicate))
        }
    }

//    fun recursiveFindFirst(predicate: (FSNode) -> Boolean): FSNode? {
//        if(predicate(this)) {
//            return this
//        }
//
//        children.forEach {
//            it.recursiveFindFirst(predicate)?.run {
//                return this
//            }
//        }
//
//        return null
//    }

    override fun toString(): String {
        return name
    }
}

fun constructFS(lines: List<String>): FSNode {
    val root = FSNode()
    var currentNode = root

    var counter = 1

    while(counter < lines.size) {
        val line = lines[counter]

        if(line.startsWith("$ ls")) {
            counter++
            val children = buildList {
                while(true) {
                    val (size, name) = lines[counter].split(" ")

                    add(FSNode(
                        name = name,
                        type = if(size == "dir") FSNode.Type.DIRECTORY else FSNode.Type.FILE,
                        size = if(size == "dir") 0L else size.toLong(),
                        parent = currentNode
                    ))

                    if(size != "dir") {
                        var it: FSNode? = currentNode
                        do {
                            it!!.size += size.toLong()
                            it = it!!.parent
                        } while(it != null)
                    }

                    if(counter >= lines.size - 1 || lines[++counter].startsWith("$")) {
                        counter--
                        break
                    }
                }
            }

            currentNode.addChildren(children)
        } else if(line.startsWith("$ cd")){
            val (_,_,name) = line.split(" ")

            currentNode = if(name == "..") {
                currentNode.parent ?: throw Exception("No parent node available. Current node: ${currentNode.name}")
            } else {
                currentNode.children.firstOrNull { it.name == name } ?: throw Exception("Child node '$name' not found")
            }
        }

        counter++
    }


    return root
}

fun part1(lines: List<String>): Long {
    val fs = constructFS(lines)
    return fs.recursiveFind { it.type == FSNode.Type.DIRECTORY && it.size <= 100000 }.sumOf { it.size }
}

fun part2(lines: List<String>): Long {
    val fs = constructFS(lines)
    val target = 30000000 - (70000000 - fs.size)

    return fs.recursiveFind { it.type == FSNode.Type.DIRECTORY && it.size >= target }.minBy { it.size }.size
}

fun main() {
    val testInput = readInput("day07/test")
    checkResult(part1(testInput), 95437)
    checkResult(part2(testInput), 24933642)

    val input = readInput("day07/input")
    println("Part 1: ${part1(input)}")
    println("Part 2: ${part2(input)}")
}
