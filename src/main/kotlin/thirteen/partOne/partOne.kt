package thirteen.partOne

import extension.resourceAsText
import extension.splitByNewline

val testInput = "/thirteen/testInput.txt".resourceAsText().splitByNewline()
val input = "/thirteen/input.txt".resourceAsText().splitByNewline()

fun findNearestAbove(interval: Int, until: Int): Int {
    var counter = 0
    while (counter <= until) {
        counter += interval
    }

    return counter
}

fun runProgram(input: List<String>) {
    val earliest = input[0].toInt()
    val departures = input[1].split(",")
        .filter { it != "x" }
        .map { it.toInt() }

    val closestDepartures = departures
        .map { it to findNearestAbove(it, earliest) }

    val closest = closestDepartures.minByOrNull { it.second }!!
    val res = (closest.second - earliest) * closest.first

    println("Result is: $res")
}

fun main() = runProgram(input)