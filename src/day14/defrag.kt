package day14

import common.positionRC.Move
import day10.round2Hash as knotHash

fun main(args: Array<String>) {
    val key = "jzgqcdpd"
    val blocks = (0 until 128).map { row -> blocks(knotHash("$key-$row"))}

    blocks.sumBy { b -> b.count { it == '1' } }.let(::println)

    val regions = countRegions(blocks)
    println(regions)

    blocks.forEach(::println)
}

fun countRegions(blocks: List<StringBuilder>): Int {
    var count = 0
    blocks.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { colIndex, cell ->
            if (cell == '1') {
                count++
                markAllNeighboursReached(blocks, rowIndex, colIndex)
            }
        }
    }
    return count
}



fun markAllNeighboursReached(blocks: List<StringBuilder>, rowIndex: Int, colIndex: Int) {
    blocks[rowIndex][colIndex] = '#'
    for (d in Move.values) {
        val r = rowIndex + d.dr
        val c = colIndex + d.dc
        if (r in blocks.indices && c in blocks[0].indices && blocks[r][c] == '1')
            markAllNeighboursReached(blocks, r, c)
    }
}

fun blocks(hash: List<Int>): StringBuilder {
    return hash.joinTo(StringBuilder(), "") { it.toString(2).padStart(8, '0').replace('0', ' ') }
}