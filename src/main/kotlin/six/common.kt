package six

import extension.resourceAsText
import extension.splitByBlankLine

fun getChars(s: String): Set<String> {
    val matches = Regex("\\w").findAll(s)
    return matches.toList()
        .map { it.value }
        .toSet()
}

fun groupChars(s: String): List<Set<String>> = s
    .split("\n")
    .filter { it.isNotBlank() }
    .map { getChars(it) }

fun unionAnswers(groupAnswers: List<Set<String>>): Int {
    val groupCount = mutableMapOf<String, Int>()

    groupAnswers.forEach { personAnswers ->
        personAnswers.forEach { personAnswer ->
            groupCount[personAnswer] = groupCount.getOrPut(personAnswer, { 0 }) + 1
        }
    }

    return groupCount
        .filter { it.value == groupAnswers.size }
        .count()
}

fun input() = "/six/input.txt".resourceAsText().splitByBlankLine()
