package eighteen.partOne

import extension.resourceAsText
import extension.splitByNewline
import java.util.*

val input = "/eighteen/input.txt".resourceAsText().splitByNewline()
val testInput = "/eighteen/testInput.txt".resourceAsText().splitByNewline()
val testInput2 = "/eighteen/testInput2.txt".resourceAsText().splitByNewline()
val testInput3 = "/eighteen/testInput3.txt".resourceAsText().splitByNewline()
val testInput4 = "/eighteen/testInput4.txt".resourceAsText().splitByNewline()

fun precedence(ch: Char): Int {
    return when (ch) {
        '+', '*' -> 1
        else -> 0
    }
}

fun infixToPostFix(sequence: String): List<Char> {
    val stack = Stack<Char>()
    val postFix = mutableListOf<Char>()

    for (char in sequence) {
        when (char) {
            '(' -> stack.push(char)
            ')' -> {
                while (stack.peek() != '(') {
                    postFix += stack.pop()
                }
                // remove '('
                stack.pop()
            }
            '+', '*' -> {
                if (stack.empty() || precedence(char) > precedence(stack.peek())) {
                    stack.push(char)
                } else {
                    while (stack.isNotEmpty() && stack.peek() != '(' && precedence(char) <= precedence(stack.peek())) {
                        postFix += stack.pop()
                    }
                    stack.push(char)
                }
            }
            else -> postFix += char
        }
    }

    while (stack.isNotEmpty()) {
        postFix += stack.pop()
    }

    return postFix
}

fun evalPostFix(expression: List<Char>): Long {
    val stack = Stack<Long>()

    for (expr in expression) {
        when (expr) {
            '*' -> stack.push(stack.pop() * stack.pop())
            '+' -> stack.push(stack.pop() + stack.pop())
            else -> stack.push(expr.toString().toLong())
        }
    }

    return stack.pop()
}

fun runProgram(input: List<String>) {
    val res = input.map { it.replace(" ", "") }
        .map { infixToPostFix(it) }
        .map { evalPostFix(it) }
        .sum()

    println(res)
}

fun main() = runProgram(input)