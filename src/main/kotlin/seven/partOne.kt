package seven

fun main() {
    val result = allBags.filter { it.name != shinyGold.name }
        .filter { isBagInTree(it, shinyGold) }
        .count()

    println("Result: $result")
}
