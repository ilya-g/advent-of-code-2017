package day10

fun main(args: Array<String>) {
    val input = "76,1,88,148,166,217,130,0,128,254,16,2,130,71,255,229"

    println(twistKnots(listOf(3, 4, 1, 5), 5))

    round1(input)

    round2("")
    round2("AoC 2017")
    round2("1,2,3")
    round2("1,2,4")

    round2(input)

}

fun round1(input: String) {
    val lengths = input.split(",").map { it.trim().toInt() }
    val elements = twistKnots(lengths)
    println(elements)
    println(elements.let { (a, b) -> a * b })
}

fun round2Hash(input: String): List<Int> {
    val lengths = input.map { it.toInt() } + listOf(17, 31, 73, 47, 23)
    val elements = twistKnots(lengths, rounds = 64)
    val dense = elements.chunked(16) { it.reduce { acc, e -> acc xor e } }
    return dense
}

fun round2(input: String) {
    println(round2Hash(input).joinToString("") { it.toString(16).padStart(2, '0') })
}

fun twistKnots(lengths: List<Int>, elements: Int = 256, rounds: Int = 1): List<Int> {
    val elementList = MutableList(elements) { it }
    var pos = 0
    var skip = 0
    repeat(rounds) {
        for (l in lengths) {
            elementList.circularView(pos, l).reverse()
    //        println("$pos, $l, $skip, $elements")
            pos += l + skip++
        }
    }
    return elementList
}


fun <T> MutableList<T>.circularView(start: Int, size: Int) = object : AbstractMutableList<T>() {
    init {
        require(this@circularView.isNotEmpty())
        require(start >= 0)
    }
    override fun add(index: Int, element: T) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeAt(index: Int): T {
        throw UnsupportedOperationException("not implemented")
    }

    fun transformIndex(index: Int): Int {
        require(index in indices)
        return (start + index) % this@circularView.size
    }

    override val size = size
    override fun get(index: Int): T = this@circularView[transformIndex(index)]
    override fun set(index: Int, element: T): T = this@circularView.set(transformIndex(index), element)

}