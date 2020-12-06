package six

fun main() {
    val res = input()
        .map { getChars(it) }
        .sumBy { it.size }

    println("Res is: $res")
}
