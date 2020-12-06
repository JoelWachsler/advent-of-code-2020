package one

fun main() {
    val searchingFor = 2020
    val input = input().split("\n")
        .map { it.toInt() }

    input.forEach { value1 ->
        input.forEach { value2 ->
            input.forEach { value3 ->
                if ((value1 + value2 + value3) == searchingFor) {
                    println("Found it: ${value1 * value2 * value3}")
                }
            }
        }
    }
}