package six

fun main() {
    val res = input()
        .map { groupChars(it) }
        .map { unionAnswers(it) }
        .sum()

    println("Res is: $res")
}
