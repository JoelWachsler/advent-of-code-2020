package seven

import extension.resourceAsText
import extension.splitByNewline

val bagContainingOtherBagsRegex = Regex("(\\w+ \\w+) bags contain (?:(\\d+) (\\w+ \\w+) bags?,?\\s?)?(?:(\\d+) (\\w+ \\w+) bags?,?\\s?)?(?:(\\d+) (\\w+ \\w+) bags?,?\\s?)?(?:(\\d+) (\\w+ \\w+) bags?,?\\s?)?\\.")
val bagContainingNoOtherBagsRegex = Regex("(\\w+ \\w+) bags contain no other bags\\.")

fun containsNoOtherBags(input: String): Pair<String, Map<String, Int>>? {
    val res = bagContainingNoOtherBagsRegex.find(input)

    return if (res != null) {
        Pair(res.groups[1]?.value ?: throw Exception("Failure!"), mapOf())
    } else {
        null
    }
}

fun parseEntry(input: String): Pair<String, Map<String, Int>> {
    val regexMatch = bagContainingOtherBagsRegex.findAll(input)

    val groups = regexMatch.toList()
        .map { it.groups }
        .flatten()

    val contains = HashMap<String, Int>()

    for (i in 2 until groups.size) {
        // Each other
        if (i % 2 == 1) {
            continue
        }

        val amountAsString = groups[i]?.value
        val type = groups[i + 1]?.value

        if (amountAsString != null && type != null) {
            contains[type] = amountAsString.toInt()
        }
    }

    return Pair(
        groups[1]?.value ?: throw Exception("Failure!"),
        contains
    )
}

fun isBagInTree(root: Bag, bag: Bag): Boolean {
    return if (root.name == bag.name) {
        true
    } else {
        root.findInnerBags()
            .any { isBagInTree(it, bag) }
    }
}

fun countBagsInTree(root: Bag): Int {
    return root.findInnerBagsWithCount()
        .map { countBagsInTree(it.first) * it.second }
        .sum() + 1
}

class Bag(
    private val all: List<Pair<String, Map<String, Int>>>,
    val name: String,
    private val innerBags: Map<String, Int>
) {

    fun findInnerBags(): List<Bag> {
        return innerBags
            .map {
                val innerBag = findBag(it.key)
                Bag(all, innerBag.first, innerBag.second)
            }
    }

    fun findInnerBagsWithCount(): List<Pair<Bag, Int>> {
        return innerBags
            .map {
                val innerBag = findBag(it.key)
                Pair(Bag(all, innerBag.first, innerBag.second), it.value)
            }
    }

    private fun findBag(withName: String): Pair<String, Map<String, Int>> {
        return all.find { it.first == withName }!!
    }
}

val lines = "/seven/input.txt".resourceAsText().splitByNewline()
val parsedEntries = lines.map { containsNoOtherBags(it) ?: parseEntry(it) }
val allBags = parsedEntries.map { Bag(parsedEntries, it.first, it.second) }
val shinyGold = allBags.find { it.name == "shiny gold" }!!
