package four

fun main() {
    val parsedPassports = input().map { convertToMap(it) }

    val entriesToValidate = listOf(
        "byr",
        "iyr",
        "eyr",
        "hgt",
        "hcl",
        "ecl",
        "pid"
//        "cid"
    )

    val result = parsedPassports
        .count { extendedValidation(entriesToValidate, it) }

    println("Result is: $result")
}

