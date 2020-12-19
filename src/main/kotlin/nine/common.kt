package nine

import extension.resourceAsText
import extension.splitByNewline

val input = "/nine/input.txt".resourceAsText().splitByNewline()

fun isNumberSumOfItems(number: Long, numbers: List<Long>): Boolean {
    val results = ArrayList<Long>()

    for (i in numbers.indices) {
        for (j in numbers.indices) {
            if (i == j) {
                continue
            }

            val first = numbers[i]
            val second = numbers[j]
            val res = first + second
            results.add(res)
            if (res == number) {
                return true
            }
        }
    }

    return false
}

fun nextSlice(currentSlice: Pair<Int, Int>, sliceSize: Int = 25): Pair<Int, Int> {
    val from = currentSlice.first
    val to = currentSlice.second
    val nextTo = to + 1

    return if ((nextTo - from) > sliceSize) {
        Pair(from + 1, nextTo)
    } else {
        Pair(from, nextTo)
    }
}

val inputAsLong = input.map { it.toLong() }
