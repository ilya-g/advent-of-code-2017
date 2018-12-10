package day16

import common.parsers.groups
import common.parsers.regexParsers
import java.io.File
import java.util.*

sealed class Move {
    data class Spin(val n: Int) : Move()
    data class Exchange(val p1: Int, val p2: Int) : Move()
    data class Partner(val n1: Char, val n2: Char) : Move()
}

fun <T> MutableList<T>.rotate(n: Int) = Collections.rotate(this, n)
fun <T> MutableList<T>.swap(p1: Int, p2: Int) = Collections.swap(this, p1, p2)
fun List<Char>.string() = joinToString("")

fun main() {
    val input = File("src/day16/input.txt").readText()

    val moveParsers = regexParsers<Move> {
        val int = groupParser(String::toInt)
        val char = groupParser(String::single)
        pattern("s(\\d+)", groups(int, Move::Spin))
        pattern("x(\\d+)/(\\d+)", groups(int, int, Move::Exchange))
        pattern("p([a-z])/([a-z])", groups(char, char, Move::Partner))
    }
    val moves = input.splitToSequence(',').map(moveParsers::parse).toList()

    val programs = ('a'..'p').toList()
    println(programs.string())
    println(danceRound(moves, programs.toMutableList()).string())


    val periodLength = run {
        val dancingPrograms = programs.toMutableList()
        (1..1000_000).first {
            danceRound(moves, dancingPrograms)
            dancingPrograms == programs
        }
    }
    val remainder = 1_000_000_000 % periodLength

    println("Period: $periodLength, remainder: $remainder")

    run {
        val dancingPrograms = programs.toMutableList()
        repeat(remainder) {
            danceRound(moves, dancingPrograms)
        }
        dancingPrograms.string().let(::println)
    }

}

fun danceRound(moves: List<Move>, programs: MutableList<Char>): MutableList<Char> {
    for (move in moves) {
        when (move) {
            is Move.Spin -> programs.rotate(move.n)
            is Move.Exchange -> programs.swap(move.p1, move.p2)
            is Move.Partner -> programs.swap(programs.indexOf(move.n1), programs.indexOf(move.n2))
        }
//        print(programs.joinToString("", postfix = "  <- "))
//        println(move)

    }
    return programs
}
