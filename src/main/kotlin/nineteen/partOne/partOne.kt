package nineteen.partOne

import extension.resourceAsText
import extension.splitByBlankLine
import extension.splitByNewline

val input = "/nineteen/input.txt".resourceAsText().splitByBlankLine()
val testInput = "/nineteen/testInput.txt".resourceAsText().splitByBlankLine()
val testInput2 = "/nineteen/testInput2.txt".resourceAsText().splitByBlankLine()

fun generateRegex(currentInstruction: Int, instructions: Map<Int, String>): String {
    val res = instructions[currentInstruction]!!.split(" ").joinToString("") {
        val instr = it.toIntOrNull()
        if (instr == null) {
            it
        } else {
            generateRegex(instr, instructions)
        }
    }

    return "($res)"
}

fun runProgram(input: List<String>) {
    val instructions = input[0]
        .splitByNewline()
        .map { it.split(": ") }
        .map { it[0].toInt() to it[1].replace("\"", "") }
        .toMap()

    val toValidate = input[1].splitByNewline()
    val regex = Regex(generateRegex(0, instructions))

    val valid = toValidate.filter { it.matches(regex) }

    println("Result: ${valid.size}")
}

fun main() = runProgram(input)
