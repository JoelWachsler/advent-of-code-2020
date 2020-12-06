package three

fun main() {
    val combinations = listOf(
        Pair(1, 1),
        Pair(3, 1),
        Pair(5, 1),
        Pair(7, 1),
        Pair(1, 2),
    )

    val playingField = input()
    var total = 1
    combinations.forEach { total *= findTreesHit(playingField, it.first, it.second) }

    println("Res: $total")
}
