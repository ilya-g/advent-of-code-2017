package day3

import kotlin.coroutines.experimental.buildSequence
import kotlin.math.abs

fun main(args: Array<String>) {
    val input = 347991
    println(findPositionOfCell(23))
    println(findPositionOfCell(1024))
    println(findPositionOfCell(input).distance())

    println(spiral().take(25).toList())
    println(sumSpiral().find { it > input })
}

data class Point(val x: Int, val y: Int)
fun Point.move(dx: Int, dy: Int) = copy(x + dx, y + dy)


fun Point.distance() = abs(x) + abs(y)
fun findPositionOfCell(cell: Int): Point {
    val spiral = findSpiralOfCell(cell)
    val start = spiralEnd(spiral - 1)
    val run = spiral * 2
    val ranges = (0..4).map { i -> start + i * run }.zipWithNext { a, b -> (a + 1)..b }
    val (r1, r2, r3, r4) = ranges

    return when (cell) {
        in r1 -> Point(spiral, spiral - 1 - (cell - r1.start))
        in r2 -> Point(spiral - 1 - (cell - r2.start), -spiral)
        in r3 -> Point(-spiral, -spiral + 1 + (cell - r3.start))
        in r4 -> Point(-spiral + 1 + (cell - r4.start), spiral)
        else -> error("cell $cell must be in ${r1.start}..${r4.endInclusive}")
    }
}

fun findSpiralOfCell(cell: Int): Int {
    if (cell == 1) return 0
    return generateSequence(1) { it + 1 }.first { spiral ->
        cell in (spiralEnd(spiral - 1) + 1)..spiralEnd(spiral)
    }
}

fun spiral() = buildSequence<Point> {
    var pos = Point(0, 0)
    var spiral = 0
    var dx = 1
    var dy = 0

    while (true) {
        yield(pos)
        when {
            pos.x + dx > spiral -> {
                dx = 0
                dy = -1
                pos = pos.move(1, 1)
                spiral += 1
            }
            pos.x + dx < -spiral -> {
                dx = 0
                dy = 1
            }
            pos.y + dy > spiral -> {
                dx = 1
                dy = 0
            }
            pos.y + dy < -spiral -> {
                dx = -1
                dy = 0
            }
        }
        pos = pos.move(dx, dy)
    }
}

fun sumSpiral() = buildSequence<Int> {
    val grid = mutableMapOf<Point, Int>()
    grid[Point(0, 0)] = 1

    for (point in spiral().drop(1)) {
        grid[point] = point.neighbours().sumBy { grid[it] ?: 0 }.also { yield(it) }
    }
}

fun Point.neighbours() = buildSequence<Point> {
    for (dx in -1..1)
        for (dy in -1..1)
            yield(move(dx, dy))
}

fun spiralEnd(spiral: Int) = (1 + spiral * 2).sqr()
fun Int.sqr() = this * this