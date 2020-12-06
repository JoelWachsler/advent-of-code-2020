package five

fun main() {
    val res = input().split("\n")
        .map { toSeatId(it) }
        .maxOrNull()!!

    println("Res is: $res")
}
