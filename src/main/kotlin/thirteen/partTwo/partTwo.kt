package thirteen.partTwo

import extension.resourceAsText
import extension.splitByNewline
import java.math.BigInteger

val testInput = "/thirteen/testInput.txt".resourceAsText().splitByNewline()
val testInput2 = "/thirteen/testInput2.txt".resourceAsText().splitByNewline()
val testInput3 = "/thirteen/testInput3.txt".resourceAsText().splitByNewline()
val testInput4 = "/thirteen/testInput4.txt".resourceAsText().splitByNewline()
val testInput5 = "/thirteen/testInput5.txt".resourceAsText().splitByNewline()
val input = "/thirteen/input.txt".resourceAsText().splitByNewline()

data class Bus(val interval: BigInteger, val offset: BigInteger)

fun match(busses: List<Bus>, timestampStart: BigInteger, increment: BigInteger): BigInteger {
    if (busses.isEmpty()) {
        return timestampStart
    }

    val busToMatch = busses[0]
    var timestamp = timestampStart

    while (true) {
        if ((timestamp + busToMatch.offset) % busToMatch.interval == BigInteger.ZERO) {
            return match(busses.subList(1, busses.size), timestamp, increment * busToMatch.interval)
        }

        timestamp += increment
    }
}

fun runProgram(input: List<String>) {
    val departures = input[1].split(",").mapIndexedNotNull { index, s ->
        if (s != "x") {
            Bus(s.toBigInteger(), index.toBigInteger())
        } else {
            null
        }
    }

    val res = match(
        departures.subList(1, departures.size),
        departures[0].interval,
        departures[0].interval
    )

    println("Found it!")
    println(res)

}

fun main() = runProgram(input)
