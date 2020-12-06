package two

import extension.resourceAsText
import extension.splitByNewline

data class Rule(val from: Int, val to: Int, val char: String)

data class Context(val rule: Rule, val password: String)

fun parseContext(entry: String): Context {
    val r = Regex("(\\d+)-(\\d+) (\\w+): (\\w+)")
    val (from, to, char, password) = r.find(entry)!!.destructured

    return Context(Rule(from.toInt(), to.toInt(), char), password)
}

fun isValid(context: Context): Boolean {
    val containsAmount = context.password.count { it.toString() == context.rule.char }

    return when {
        containsAmount > context.rule.to -> {
            false
        }
        containsAmount < context.rule.from -> {
            false
        }
        else -> {
            true
        }
    }
}

fun isValidExtended(context: Context): Boolean {
    val first = context.password[context.rule.from - 1].toString() == context.rule.char
    val second = context.password[context.rule.to - 1].toString() == context.rule.char

    return first xor second
}

fun input() = "/two/input.txt".resourceAsText().splitByNewline()
