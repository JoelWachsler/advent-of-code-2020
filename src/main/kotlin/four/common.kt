package four

import extension.getResourceAsText
import extension.splitByBlankLine

fun convertToMap(entry: String) = entry
    .replace("\n", " ").split(" ")
    .filter { it.isNotBlank() }
    .map { it.split(":") }
    .associateBy({ it[0] }, { it[1] })

fun validates(entriesToValidate: List<String>, entry: Map<String, String>) = entriesToValidate
    .all { entry.containsKey(it) }

fun extendedValidation(entriesToValidate: List<String>, entry: Map<String, String>): Boolean {
    if (!validates(entriesToValidate, entry)) {
        return false
    }

    val byr = entry["byr"]!!
    if (!fullStrValidation(byr, 4, 1920, 2002)) {
        return false
    }

    val iyr = entry["iyr"]!!
    if (!fullStrValidation(iyr, 4, 2010, 2020)) {
        return false
    }

    val eyr = entry["eyr"]!!
    if (!fullStrValidation(eyr, 4, 2020, 2030)) {
        return false
    }

    val hgt = entry["hgt"]!!
    val isCm = hgt.endsWith("cm")
    val hgtWithoutSuffix = hgt
        .removeSuffix("cm")
        .removeSuffix("in")
    if (isCm) {
        if (!validateInt(hgtWithoutSuffix, 150, 193)) {
            return false
        }
    } else {
        if (!validateInt(hgtWithoutSuffix, 59, 76)) {
            return false
        }
    }

    val hcl = entry["hcl"]!!
    if (!hcl.matches(Regex("#[0-9a-f]{6}"))) {
        return false
    }

    val ecl = entry["ecl"]!!
    val validEcl = listOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth")
    if (!validEcl.contains(ecl)) {
        return false
    }

    val pid = entry["pid"]!!
    if (!pid.matches(Regex("[0-9]{9}"))) {
        return false
    }

//    val cid = entry["cid"]!!

    return true
}

fun fullStrValidation(str: String, len: Int, min: Int, max: Int) = validateLength(str, len) && validateInt(str, min, max)

fun validateLength(str: String, len: Int): Boolean = str.length == len

fun validateInt(str: String, min: Int, max: Int): Boolean {
    val byrAsInt = str.toInt()
    if (byrAsInt > max) {
        return false
    }
    if (byrAsInt < min) {
        return false
    }
    return true
}

fun input() = "/four/input.txt".getResourceAsText().splitByBlankLine()


