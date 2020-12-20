package ten

fun main() {
    val formattedInput = input
        .map { it.toInt() }
        .toMutableList()

    val finalAdapter = formattedInput.maxOrNull()!! + 3
    formattedInput.add(finalAdapter)

    val sortedInput = formattedInput.sorted()
    val combinations = findAdapterCombinations(sortedInput, 0, finalAdapter)
    println("Got: $combinations")
}

fun findAdapterCombinations(
    sortedInput: List<Int>,
    currentValue: Int,
    maxValue: Int,
    memoize: MutableMap<Int, Long> = mutableMapOf()
): Long {
    return if (currentValue == maxValue) {
        1
    } else {
        sortedInput
            .filter { it > currentValue }
            .filter { (it - currentValue) <= 3 }
            .map {
                if (memoize[it] == null) {
                    memoize[it] = findAdapterCombinations(sortedInput, it, maxValue, memoize)
                }
                memoize[it]!!
            }
            .sum()
    }
}

