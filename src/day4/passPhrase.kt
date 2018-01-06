package day4

import java.io.File

fun main(args: Array<String>) {
    val phrases = File("src/day4/input.txt").readLines().map { it.split(" ") }

    val validPhrases = phrases.count {
        it.toSet().size == it.size
    }.also(::println)

    val validPhrases2 = phrases.count {
        it.map { w -> w.toCharArray().sorted() }.let { it.toSet().size == it.size }
    }.also(::println)
}