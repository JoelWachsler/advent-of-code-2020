package nine

fun findSequenceWithSum(len: Int, sequence: List<Long>, searchingFor: Long): List<Long>? {
    for (i in 0 until sequence.size - len) {
        val entries = arrayListOf<Long>()

        for (j in 0..len) {
            entries.add(sequence[j + i])
        }

        val sumOfEntries = entries.sum()
        if (sumOfEntries == searchingFor) {
            return entries
        }
    }

    return null
}

fun main() {
    val numberToFind: Long = 57195069

    for (i in 2..inputAsLong.size) {
        val res = findSequenceWithSum(i, inputAsLong, numberToFind)
        if (res != null) {
            println("Found it: $res")
            val min = res.minOrNull()!!
            val max = res.maxOrNull()!!
            val answer = min + max
            println("And the result is: $answer")
            break
        }
    }
}
