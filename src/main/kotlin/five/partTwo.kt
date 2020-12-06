package five

fun main() {
    val res = input().split("\n")
        .map { toSeatId(it) }
        .sorted()

    res.forEachIndexed { index, entry ->
        val prev = index - 1
        val next = index + 1

        when {
            prev < 0 -> {
            }
            next > res.lastIndex -> {
            }
            else -> {
                if ((res[prev] + 1) != entry || entry != (res[next] - 1)) {
                    println("Found mismatch!")
                    println("prev: ${res[prev]}, current: ${entry}, next: ${res[next]}")
                }
            }
        }
    }
}