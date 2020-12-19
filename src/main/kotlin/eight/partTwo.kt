package eight

fun main() {
    val inp = input

    for (i in inp.indices) {
        val programCpy = inp.toMutableList()
        val lineToChange = programCpy[i]

        if (lineToChange is Acc) {
            continue
        }

        programCpy[i] = Jmp(lineToChange.input)
        val testProgram = Program(programCpy)
        if (testProgram.execute()) {
            println("Success!!")
            println(testProgram)
            break
        }
        programCpy[i] = Nop(lineToChange.input)
        val testProgram2 = Program(programCpy)
        if (testProgram2.execute()) {
            println("Success!!")
            println(testProgram2)
            break
        }
    }
}
