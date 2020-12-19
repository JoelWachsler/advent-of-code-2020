package nine

fun main() {
    var currentSlice = Pair(0, 25)

    while (true) {
        val number = inputAsLong[currentSlice.second]
        if (!isNumberSumOfItems(number, inputAsLong.subList(currentSlice.first, currentSlice.second))) {
            println("Failure on: $currentSlice, and the number is: $number")
            break
        }

        currentSlice = nextSlice(currentSlice, 25)
    }
}
