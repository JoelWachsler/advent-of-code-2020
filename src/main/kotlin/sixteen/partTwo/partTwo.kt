package sixteen.partTwo

import extension.resourceAsText
import extension.splitByBlankLine
import extension.splitByNewline

val input = "/sixteen/input.txt".resourceAsText().splitByBlankLine()
val testInput = "/sixteen/testInput.txt".resourceAsText().splitByBlankLine()
val testInput2 = "/sixteen/testInput2.txt".resourceAsText().splitByBlankLine()

data class Rule(val name: String, val ranges: List<IntRange>)
fun Rule.containsValue(value: Int) = ranges.any { range -> range.contains(value) }

data class Row(val rowId: Int, val rules: List<Rule>, val values: List<Int>)
fun Row.withoutInvalidRules(): Row {
    val validRules = rules.filter { rule -> values.all { value -> rule.containsValue(value) } }
    return copy(rules = validRules)
}
fun Row.removeRule(rule: Rule): Row {
    val filteredRules = rules.filter { it.name != rule.name }
    return copy(rules = filteredRules)
}
fun Row.hasSingleRule() = rules.size == 1
fun Row.removeRuleIfNotSingle(rule: Rule): Row {
    return if (hasSingleRule()) {
        this
    } else {
        removeRule(rule)
    }
}
fun Row.removeRulesIfNotSingle(rules: List<Rule>): Row {
    var row = this
    for (rule in rules) {
        row = row.removeRuleIfNotSingle(rule)
    }
    return row
}

fun runProgram(input: List<String>) {
    val rangeRegex = Regex("(\\w+\\s?\\w+?): (\\d+)-(\\d+) or (\\d+)-(\\d+)")

    val rules = input[0].splitByNewline()
    val parsedRules = rules.map {
        val groups = rangeRegex.find(it)!!.groups
        Rule(
            groups[1]!!.value,
            listOf(
                IntRange(groups[2]!!.value.toInt(), groups[3]!!.value.toInt()),
                IntRange(groups[4]!!.value.toInt(), groups[5]!!.value.toInt()),
            ),
        )
    }

    val splitByNewline = input[2]
        .replace("nearby tickets:", "")
        .splitByNewline()
    val nearbyTickets = splitByNewline
        .map { ticket ->
            ticket
                .split(",")
                .filter { it.isNotBlank() }
                .map { it.toInt() }
        }
        .filter { it.isNotEmpty() }

    val flattenedRules = parsedRules.flatMap { it.ranges }

    val invalidValues = splitByNewline
        .flatMap { it.split(",") }
        .filter { it.isNotBlank() }
        .map { it.toInt() }
        .filter { value -> flattenedRules.none { rule -> rule.contains(value) } }

    val validTickets = nearbyTickets
        .filter { it.none { value -> invalidValues.contains(value) } }

    // We need by column and not by row - let's invert
    val validTicketInverse = MutableList(validTickets[0].size) { MutableList(validTickets.size) { 0 } }
    for (i in validTickets.indices) {
        for (j in validTickets[i].indices) {
            validTicketInverse[j][i] = validTickets[i][j]
        }
    }

    val rows = validTicketInverse.mapIndexed { index, ticketRow -> Row(index, parsedRules, ticketRow) }
    val validRows = rows.map { it.withoutInvalidRules() }
    val rowTypes = determineRowType(validRows)
        .map { it.rules.first().name to it.rowId }

    val ourTicket = input[1]
        .replace("your ticket:", "")
        .splitByNewline()
        .flatMap { it.split(",") }
        .filter { it.isNotBlank() }
        .map { it.toInt() }

    val departureRows = rowTypes.filter { it.first.contains("departure") }

    val res = departureRows.map { row -> ourTicket[row.second] }
        .map { it.toLong() }
        .reduce { a, b -> a * b }

    println("Result: $res")
}

fun determineRowType(rows: List<Row>): List<Row> {
    if (rows.isEmpty()) {
        return listOf()
    }

    val completedRows = rows.filter { it.hasSingleRule() }
    val completedRules = completedRows.flatMap { it.rules }
    val rowsToFurtherFilter = rows.filter { !it.hasSingleRule() }

    val filteredRows = rowsToFurtherFilter.map { it.removeRulesIfNotSingle(completedRules) }
    return listOf(completedRows, determineRowType(filteredRows)).flatten()
}

fun main() = runProgram(input)
