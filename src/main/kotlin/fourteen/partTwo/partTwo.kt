package fourteen.partTwo

import extension.resourceAsText
import extension.splitByNewline

val input = "/fourteen/input.txt".resourceAsText().splitByNewline()
val testInput = "/fourteen/testInput.txt".resourceAsText().splitByNewline()
val testInput2 = "/fourteen/testInput2.txt".resourceAsText().splitByNewline()

fun Long.toBoolArray(): BooleanArray {
    val array = BooleanArray(36)
    for (i in array.indices) {
        array[i] = ((this shr i) and 1) == 1L
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

fun createMask(mask: String, value: BooleanArray, index: Int = 0): List<BooleanArray> {
    if (index >= value.size) {
        return listOf(value)
    }

    return when (mask[index]) {
        'X' -> {
            val firstArray = value.copyOf()
            firstArray[index] = true
            val secondArray = value.copyOf()
            secondArray[index] = false

            listOf(
                createMask(mask, firstArray, index + 1),
                createMask(mask, secondArray, index + 1),
            ).flatten()
        }
        '1' -> {
            val newBooleanArray = value.copyOf()
            newBooleanArray[index] = true
            createMask(mask, newBooleanArray, index + 1)
        }
        '0' -> {
            val newBooleanArray = value.copyOf()
            newBooleanArray[index] = value[index]
            createMask(mask, newBooleanArray, index + 1)
        }
        else -> error("Invalid...")
    }
}

fun runProgram(input: List<String>) {
    val memory = mutableMapOf<Long, BooleanArray>()
    var currentMask = ""
    val maskRegex = Regex("([X|0|1]+)")
    val instructionRegex = Regex("mem\\[(\\d+)\\] = (\\d+)")

    for (line in input) {
        if (line.startsWith("mask")) {
            currentMask = maskRegex.find(line)!!.value
        } else {
            val res = instructionRegex.find(line)!!.groupValues
            val valueToWrite = res[2].toLong().toBoolArray()
            val addressAsLong = res[1].toLong()
            val addressesToWriteTo = createMask(currentMask, addressAsLong.toBoolArray())

            addressesToWriteTo.forEach { address -> memory[address.toLong()] = valueToWrite }
        }
    }

    val res = memory.map { it.value.toLong().toBigInteger() }
        .reduce { acc, bigInteger -> acc.add(bigInteger) }

    println("Result: $res")
}

fun main() = runProgram(input)
