package fifteen.partTwo

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
    val map = mutableMapOf<Int, Int>()
    for (index in 0..mutableInput.size - 2) {
        map[mutableInput[index]] = index + 1
    }

    var value = mutableInput.last()
    val initialSize = mutableInput.size - 1
    var counter = initialSize

    for (index in initialSize + 1 until 30000000) {
        val indexOfPreviousValue = map.getOrDefault(value, -1)
        if (indexOfPreviousValue == -1) {
            map[value] = index
            value = 0
        } else {
            map[value] = index
            value = index - indexOfPreviousValue
        }
        counter++
    }

    println("Result: $value")
}

fun main() = runProgram(input)
