package day8

import java.io.File
import kotlin.coroutines.experimental.buildSequence


val instructionRegex = Regex("(\\w+) (inc|dec) (-?\\d+) if (\\w+) ([<>=!]{1,2}) (-?\\d+)")

data class Instruction(val reg1: String, val action: Action, val value1: Int, val reg2: String, val cond: Condition, val value2: Int)
enum class Action { inc, dec }
enum class Condition(val mnemonic: String) {
    lt("<"), lte("<="), gt(">"), gte(">="), neq("!="), eq("==");
    fun eval(v1: Int, v2: Int) = when (this) {
            lt -> v1 < v2
            lte -> v1 <= v2
            gt -> v1 > v2
            gte -> v1 >= v2
            neq -> v1 != v2
            eq -> v1 == v2
    }
}
val conditions = Condition.values().associateBy { it.mnemonic }


fun main(args: Array<String>) {
    val lines = File("src").resolve("day8/input.txt").readLines()
    val instructions = lines.map {
        val (reg1, action, value1, reg2, cond, value2) = instructionRegex.matchEntire(it)!!.destructured
        Instruction(reg1, Action.valueOf(action), value1.toInt(),
                reg2, conditions.getValue(cond), value2.toInt())
    }

    val registers = object {
        val map = mutableMapOf<String, Int>()
        operator fun get(name: String) = map.getOrPut(name, { 0 })
        operator fun set(name: String, value: Int) { map[name] = value }
        override fun toString() = "Registers" + map
    }

    val maximums = buildSequence {
        for (i in instructions) {
            if (registers[i.reg2].let { i.cond.eval(it, i.value2) }) {
                registers[i.reg1] += (if (i.action == Action.inc) +1 else -1) * i.value1
            }
            println("$i,".padEnd(82) + registers)
            yield(registers.map.values.max()!!)
        }
    }.toList()
    val globalMax = maximums.max()!!
    val lastMax = maximums.last()

    registers.map.keys.containsAll(arrayOf("a", "b").asList())

    println(registers.map.maxBy { it.value })
    println("$globalMax, $lastMax")
}