package day2

import java.io.File

fun main(args: Array<String>) {
    val input = File("src/day2/input.txt").readLines()
    val data = input.map { line -> line.split("\\t".toRegex()).map { it.toInt() } }

    val checksum = data.sumBy { it.max()!! - it.min()!! }
    println(checksum)

    val checksum2 = data.sumBy { evenDivideResult(it) }
    println(checksum2)
}

fun evenDivideResult(line: List<Int>): Int {
    for (i1 in line.indices) {
        for (i2 in line.indices) {
            if (i1 != i2) {
                val v1 = line[i1]
                val v2 = line[i2]
                if (v1 % v2 == 0) return v1 / v2
            }
        }
    }
    error("No evenly divisible in line $line")
}