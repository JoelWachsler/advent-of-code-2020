package two

fun main() {
    val res = input().map { parseContext(it) }
        .filter { isValid(it) }
        .count()

    println("Res: $res")
}
