package day12

import java.io.File

fun main(args: Array<String>) {
    val input = File("src").resolve("day12/input.txt").readLines()

    val connections = input.associate {
        val (id, others) = it.split("<->")
        id.trim().toInt() to others.split(",").map { it.trim().toInt() }
    }

    println(connections)

    fun groupOf(id: Int): Collection<Int> {
        val visited = mutableSetOf(id)
        fun visitConnectionsOf(id: Int) {
            connections[id]!!.filter { visited.add(it) }.forEach(::visitConnectionsOf)
        }
        visitConnectionsOf(id)
        return visited
    }



    val ids = connections.keys.toMutableList()
    var groups = 0
    while (ids.isNotEmpty()) {
        val group = groupOf(ids.first())
        println("${group.size}: $group")
        groups += 1
        ids -= group
    }
    println(groups)
}
