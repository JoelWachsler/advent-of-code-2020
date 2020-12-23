package sixteen.partOne

import extension.resourceAsText
import extension.splitByBlankLine
import extension.splitByNewline

val input = "/sixteen/input.txt".resourceAsText().splitByBlankLine()
val testInput = "/sixteen/testInput.txt".resourceAsText().splitByBlankLine()

fun runProgram(input: List<String>) {
    val rangeRegex = Regex("\\w+: (\\d+)-(\\d+) or (\\d+)-(\\d+)")

    val rules = input[0].splitByNewline()
    val parsedRules = rules.map {
        val groups = rangeRegex.find(it)!!.groups
        listOf(
            IntRange(groups[1]!!.value.toInt(), groups[2]!!.value.toInt()),
            IntRange(groups[3]!!.value.toInt(), groups[4]!!.value.toInt()),
        )
    }.flatten()

    val nearbyTickets = input[2]
        .replace("nearby tickets:", "")
        .splitByNewline()
        .flatMap { it.split(",") }
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    val invalidTickets = nearbyTickets.filter { ticket -> parsedRules.none { rule -> rule.contains(ticket) } }

    println("Result: ${invalidTickets.sum()}")
}

fun main() = runProgram(input)
