package day6

import java.io.File

fun main(args: Array<String>) {
    val initialBanks = File("src/day6/input.txt").readText().split('\t').map { it.toInt() }

    println(findCycleAfter(initialBanks))
}

fun findCycleAfter(initialBanks: List<Int>): Pair<Int, Int> {
    val observed = mutableSetOf<List<Int>>()
    redistributions(initialBanks).forEachIndexed { index, state ->
        if (!observed.add(state))
            return index to (index - observed.indexOf(state))
    }
    error("Sequence should not end")
}

fun redistributions(initialBanks: List<Int>): Sequence<List<Int>> =
        generateSequence(initialBanks, ::redistribute)


fun redistribute(banks: List<Int>): List<Int> {
    val result = banks.toMutableList()
    val value = result.max()!!
    var index = result.indexOf(value)
    result[index] = 0
    repeat(times = value) {
        index = (index + 1) % result.size
        result[index] += 1
    }
    return result
}