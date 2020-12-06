package two

fun main() {
    val res = input().map { parseContext(it) }
        .filter { isValidExtended(it) }
        .count()

    println("Res: $res")
}
