package fourteen.partOne

import extension.resourceAsText
import extension.splitByNewline
import java.util.*

val input = "/fourteen/input.txt".resourceAsText().splitByNewline()
val testInput = "/fourteen/testInput.txt".resourceAsText().splitByNewline()

data class WriteInstruction(val value: Long, val writeTo: Int)

fun WriteInstruction.toBoolArray(): BooleanArray {
    val array = BooleanArray(36)
    for (i in array.indices) {
        array[i] = ((value shr i) and 1) == 1L
    }
    return array.reversedArray()
}

fun BooleanArray.toReadable(): String {
    return this.joinToString("") {
        if (it) {
            "1"
        } else {
            "0"
        }
    }
}

fun BooleanArray.toLong(): Long {
    return this.reversedArray().mapIndexed { index, b ->
        if (b) {
            1L shl index
        } else {
            0L
        }
    }.sum()
}

fun applyMask(mask: String, value: BooleanArray): BooleanArray {
    val newBooleanArray = value.copyOf()
    mask.forEachIndexed { i, c ->
        when (c) {
            'X' -> newBooleanArray[i] = value[i]
            '1' -> newBooleanArray[i] = true
            '0' -> newBooleanArray[i] = false
        }
    }

    return newBooleanArray
}

fun runProgram(input: List<String>) {
    val memory = mutableMapOf<Int, BooleanArray>()
    var currentMask = ""
    val maskRegex = Regex("([X|0|1]+)")
    val instructionRegex = Regex("mem\\[(\\d+)\\] = (\\d+)")

    for (line in input) {
        if (line.startsWith("mask")) {
            currentMask = maskRegex.find(line)!!.value
        } else {
            val res = instructionRegex.find(line)!!.groupValues
            val instruction = WriteInstruction(res[2].toLong(), res[1].toInt())
            memory[instruction.writeTo] = applyMask(currentMask, instruction.toBoolArray())
        }
    }

    val res = memory.map { it.value.toLong() }.sum()

    println("Result: $res")
}

fun main() = runProgram(input)
