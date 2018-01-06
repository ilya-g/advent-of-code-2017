package day7

import java.io.File

val itemRegex = Regex("(\\w+) \\((\\d+)\\)( -> (.+))?")

data class ProgramInfo(val name: String, val weight: Int, val nestedNames: List<String>)
data class Program(val name: String, val weight: Int, val nested: List<Program>) {
    val weightWithNested: Int by lazy {
        this.weight + nested.sumBy { it.weightWithNested }
    }
}

fun main(args: Array<String>) {
    val programs = File("src/day7/input.txt").readLines().map {
        val (name, weight, _, nested) = itemRegex.matchEntire(it)!!.destructured
        ProgramInfo(name, weight.toInt(), if (nested.isEmpty()) emptyList() else nested.split(",").map { it.trim() })
    }

    val root = buildTree(programs)
    println(root)
    checkBalanced(root)
}

fun checkBalanced(root: Program) {
    root.nested.forEach(::checkBalanced)
    val nestedWeights = root.nested.groupBy { it.weightWithNested }
    if (nestedWeights.size > 1) {
        println("Disbalance: ${nestedWeights.entries.sortedBy { it.value.size }
                .map { it.value.map { "${it.weightWithNested} (${it.weight})" }}}")
    }
}


fun buildTree(programs: List<ProgramInfo>): Program {
    val byName = mutableMapOf<String, Program>()
    var remaining = programs
    while (remaining.isNotEmpty()) {
        val (ready, notReady) = remaining.map { it to it.nestedNames.mapNotNull(byName::get) }
                .partition { (p, nested) -> nested.size == p.nestedNames.size }

        ready.map { (p, nested) -> Program(p.name, p.weight, nested) }
                .associateByTo(byName, Program::name)

        remaining = notReady.map { it.first }
    }
    return byName.values.last()
}


