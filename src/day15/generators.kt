package day15

data class Generator(val init: Int, val factor: Int, val filterMultiple: Int, val modulo: Int = 2147483647) {
    fun sequence() = sequence {
        var state = init.toLong()
        while (true) {
            state = state * factor % modulo
            yield(state.toInt())
        }
    }

    fun filteredSequence() = sequence().filter { it % filterMultiple == 0 }
}

fun countLowerBitsMatches(seqA: Sequence<Int>, seqB: Sequence<Int>, limit: Int): Int =
        (seqA zip seqB).take(limit).count { (a, b) -> a.toShort() == b.toShort() }

fun main() {

    val genA = Generator(init = 618, factor = 16807, filterMultiple = 4)
    val genB = Generator(init = 814, factor = 48271, filterMultiple = 8)

    run part1@ {
        countLowerBitsMatches(genA.sequence(), genB.sequence(), 40_000_000).let(::println)
    }

    run part2@ {
        countLowerBitsMatches(genA.filteredSequence(), genB.filteredSequence(), 5_000_000).let(::println)
    }
}