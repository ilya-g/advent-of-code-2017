package day5

import java.io.File

fun main(args: Array<String>) {
    val jumps = File("src/day5/input.txt").readLines().map { it.toInt() }

    println(findStepsToBreakOut(jumps))
    println(findStepsToBreakOut(jumps) { if (it >= 3) it - 1 else it + 1 })

}

fun findStepsToBreakOut(jumps: List<Int>, instructionMutator: (Int) -> Int = { it + 1 }): Int {
    val program = jumps.toMutableList()
    var index = 0
    var steps = 0
    while (index in program.indices) {
        index += program[index].also { program[index] = instructionMutator(program[index]) }
        steps +=1
    }
    return steps
}