package day11

import java.io.File
import kotlin.coroutines.experimental.buildSequence
import kotlin.math.*

enum class Directions(val delta: CubePos) {
    nw(CubePos(-1, +1,  0)),
    n (CubePos( 0, +1, -1)),
    ne(CubePos(+1,  0, -1)),

    sw(CubePos(-1,  0, +1)),
    s (CubePos( 0, -1, +1)),
    se(CubePos(+1, -1,  0))
}

data class CubePos(val x: Int, val y: Int, val z: Int) {
    init {
        require(x + y + z == 0)
    }

    operator fun plus(other: CubePos) = CubePos(this.x + other.x, this.y + other.y, this.z + other.z)
    operator fun minus(other: CubePos) = CubePos(this.x + other.x, this.y + other.y, this.z + other.z)
    infix fun distanceTo(other: CubePos) = (other - this).run { maxOf(abs(x), abs(y), abs(z)) }
}

fun main(args: Array<String>) {
    val input = File("src").resolve(File("day11/input.txt")).readText()
    val directions = input.split(",").map(Directions::valueOf)

    val start = CubePos(0, 0, 0)
    val positions = buildSequence { // TODO: use scan function
        var pos = start
        for (d in directions) {
            pos += d.delta
            yield(pos)
        }
    }

    println(positions.map { it distanceTo start }.max())
    println(positions.last() distanceTo start)
}