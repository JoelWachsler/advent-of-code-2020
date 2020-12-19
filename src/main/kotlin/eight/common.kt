package eight

import extension.resourceAsText
import extension.splitByNewline

interface Instruction {
    val input: Int

    fun execute(program: Program)
}

class Acc(override val input: Int) : Instruction {
    override fun execute(program: Program) {
        program.acc += input
    }
}

class Jmp(override val input: Int) : Instruction {
    override fun execute(program: Program) {
        program.line += input
    }
}

class Nop(override val input: Int) : Instruction {
    override fun execute(program: Program) {
    }
}

class Program(
    private val instructions: List<Instruction>,
    private val maxRowExecution: Int = 0
) {
    var acc = 0
    var line = 0
    private val lineExecutions = HashMap<Instruction, Int>()

    fun execute(): Boolean {
        while (true) {
            if (line == instructions.size) {
                return true
            }

            val lineToExecute = instructions[line]
            val lineExecution = lineExecutions.getOrDefault(lineToExecute, 0)

            if (lineExecution > maxRowExecution) {
                return false
            }

            lineToExecute.execute(this)
            lineExecutions[lineToExecute] = lineExecution + 1

            if (lineToExecute !is Jmp) {
                line++
            }
        }
    }

    override fun toString(): String {
        return "Program(acc=$acc, line=$line)"
    }
}

fun parseInput(lines: List<String>): List<Instruction> = lines.map {
    val split = it.split(" ")
    val instruction = split[0]
    val input = split[1].toInt()

    when (instruction) {
        "nop" -> {
            Nop(input)
        }
        "acc" -> {
            Acc(input)
        }
        "jmp" -> {
            Jmp(input)
        }
        else -> throw Exception("Failure...")
    }
}

val input = parseInput("/eight/input.txt".resourceAsText().splitByNewline())
val testInput = parseInput("/eight/test.txt".resourceAsText().splitByNewline())
