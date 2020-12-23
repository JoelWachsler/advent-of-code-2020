package fifteen.partOne

import extension.resourceAsText

val input = "/fifteen/input.txt".resourceAsText().split(",")
val testInput = "/fifteen/testInput.txt".resourceAsText().split(",")
val testInput2 = "/fifteen/testInput2.txt".resourceAsText().split(",")
val testInput3 = "/fifteen/testInput3.txt".resourceAsText().split(",")
val testInput4 = "/fifteen/testInput4.txt".resourceAsText().split(",")
val testInput5 = "/fifteen/testInput5.txt".resourceAsText().split(",")
val testInput6 = "/fifteen/testInput6.txt".resourceAsText().split(",")
val testInput7 = "/fifteen/testInput7.txt".resourceAsText().split(",")

fun runProgram(input: List<String>) {
    val mutableInput = input.map { it.toInt() }
        .toMutableList()

    for (index in mutableInput.size - 1..2020) {
        val previousValue = mutableInput[index]
        val indexOfPreviousValue = mutableInput.subList(0, index).indexOfLast { it == previousValue }
        if (indexOfPreviousValue == -1) {
            mutableInput.add(0)
        } else {
            mutableInput.add(index - indexOfPreviousValue)
        }
    }

    println("Result: ${mutableInput[2019]}")
}

fun main() = runProgram(input)
