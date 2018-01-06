package day9

import java.io.File
import java.nio.CharBuffer

fun main(args: Array<String>) {
    val input = File("src").resolve(File("day9/input.txt")).readText()
    val buffer = CharBuffer.wrap(input)
    val result = parseGroup(buffer)
    println(result)
    println(result.totalScore())
    println(result.totalGarbage())
}

fun CharBuffer.peek() = get(position())
fun CharBuffer.advance(offset: Int = 1) { position(position() + offset) }
fun CharBuffer.invalidCharacter(): Nothing = error("Invalid char at ${position()}: ${peek()}")

fun parseGroup(buffer: CharBuffer): Entry.Group {
    check(buffer.get() == '{')
    val entries = mutableListOf<Entry>()
    outer@ do {
        val current = buffer.peek()
        when (current) {
            '{' -> entries.add(parseGroup(buffer))
            '<' -> entries.add(parseGarbabe(buffer))
            '}' -> {}
            else -> buffer.invalidCharacter()
        }
        val delimiter = buffer.peek()
        val endOfGroup = when (delimiter) {
            ',' -> false
            '}' -> true
            else -> buffer.invalidCharacter()
        }
        buffer.advance()
    } while (!endOfGroup)
    return Entry.Group(entries)
}

fun parseGarbabe(buffer: CharBuffer): Entry.Garbage {
    check(buffer.get() == '<')
    var cancel = 0
    var chars = 0
    while (true) {
        val current = buffer.get()
        if (cancel > 0) {
            cancel--
        } else {
            when (current) {
                '!' -> cancel = 1
                '>' -> return Entry.Garbage(chars)
                else -> chars++
            }
        }
    }
}

sealed class Entry {
    class Group(val entries: List<Entry>) : Entry() {
        override fun toString() = "{${entries.joinToString(",")}}"

        fun totalScore(base: Int = 1): Int =
                base + entries.sumBy { if (it is Group) it.totalScore(base + 1) else 0 }

        override fun totalGarbage(): Int = entries.sumBy { it.totalGarbage() }
    }
    class Garbage(val charCount: Int) : Entry() {
        override fun toString() = "<$charCount>"
        override fun totalGarbage(): Int = charCount
    }

    abstract fun totalGarbage(): Int
}

