package day13

import java.io.File

data class Scanner(val position: Int, val direction: Int = 1)

fun main(args: Array<String>) {
    val input = File("src").resolve("day13/input.txt").readLines()

    val layers = input.map { it.split(":") }.associate { (a, b) -> a.toInt() to b.trim().toInt() }
    val layerIndices = 0..layers.keys.max()!!

    fun advanceScanners(scanners: Map<Int, Scanner>) =
            scanners.mapValues { (layer, scanner) -> (scanner.position + scanner.direction).let { newPos ->
                when {
                    newPos == 1 || newPos == layers[layer]!! -> Scanner(newPos, -scanner.direction)
                    else -> Scanner(newPos, scanner.direction)
                }
            }}

    fun advanceScanners(scanners: MutableMap<Int, Scanner>) {
        for (entry in scanners) {
            val (layer, scanner) = entry
            entry.setValue((scanner.position + scanner.direction).let { newPos ->
                when {
                    newPos == 1 || newPos == layers[layer]!! -> Scanner(newPos, -scanner.direction)
                    else -> Scanner(newPos, scanner.direction)
                }
            })
        }
    }

    fun positionAt(layer: Int, depth: Int, time: Int): Int {
        val period = (depth - 1) * 2
//        val phase = time % period
        //return if (phase < depth) phase + 1 else depth - (period - phase)
        return 1 + time % period
    }
    fun positionAt(layer: Int, time: Int): Int {
        val depth = layers[layer] ?: return 0
        return positionAt(layer, depth, time)
    }


    fun countScore(): Int {
        return layers.entries.sumBy { (layer, depth) ->
            if (positionAt(layer, depth, time = layer) == 1) layer * depth else 0
        }
    }

    fun hasHits(delay: Int): Boolean {
        return layers.any { (layer, depth) -> positionAt(layer, depth, (delay + layer)) == 1 }
//
//        val scanners = layers.mapValuesTo(mutableMapOf()) { Scanner(1) }
//        repeat(delay) { advanceScanners(scanners) }
//        return layerIndices.any { (scanners[it]?.position == 1).also { advanceScanners(scanners) } }
    }

    println(countScore())

    val delays = generateSequence(0) { it + 1 }
    println(delays.first { !hasHits(it) })
}