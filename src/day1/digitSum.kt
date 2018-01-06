package day1

import java.io.File

fun main(args: Array<String>) {

    val input = File("src/day1/input.txt").readText()
    val data = input.map { it.toString().toInt() }

    val sum1 = (data + data.first()).zipWithNext { a, b -> if (a == b) a else 0 }.sum()
    println(sum1)

    val shift = data.size / 2
    val shifted = data.drop(shift) + data
    val sum2 = data.zip(shifted) { a, b -> if (a == b) a else 0 }.sum()
    println(sum2)
}