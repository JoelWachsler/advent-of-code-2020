package ten

import extension.resourceAsText
import extension.splitByNewline
import java.util.*

val testInput = "/ten/testInput.txt".resourceAsText().splitByNewline()
val testInput2 = "/ten/testInput2.txt".resourceAsText().splitByNewline()
val input = "/ten/input.txt".resourceAsText().splitByNewline()

fun main() {
    var joltage = 0

    val formattedInput = input
        .map { it.toInt() }
        .toCollection(TreeSet())

    val diffs = mutableListOf<Int>()

    val finalAdapter = formattedInput.maxOrNull()!! + 3
    formattedInput.add(finalAdapter)

    while (formattedInput.isNotEmpty()) {
        val closestValue = formattedInput.ceiling(joltage)
            ?: throw Error("Failed on joltage: $joltage, with the following values left: $formattedInput")

        val diff = closestValue - joltage
        diffs.add(diff)
        joltage = closestValue
        formattedInput.remove(closestValue)
    }

    println("The final joltage is: $joltage, and the diffs are: $diffs")
    val counts = diffs.groupBy { it }
        .map { Pair(it.key, it.value.size) }
        .associateBy({ it.first }, { it.second })
    val res = counts[1]!! * counts[3]!!
    println("Results are: $counts, with result: $res")
}
