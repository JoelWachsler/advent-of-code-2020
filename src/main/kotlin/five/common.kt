package five

import extension.resourceAsText

fun search(s: String, from: Int, to: Int, lowerChar: Char = 'F'): Triple<String, Int, Int> {
    val diff = (to - from) / 2 + 1

    return if (s[0] == lowerChar) {
        Triple(s.drop(1), from, to - diff)
    } else {
        Triple(s.drop(1), from + diff, to)
    }
}

fun fullSearch(s: String, from: Int = 0, to: Int = 127): Pair<Int, Int> {
    if (s.isEmpty()) {
        return Pair(from, to)
    }

    return if (s[0] == 'F' || s[0] == 'B') {
        val search = search(s, from, to)
        if (search.first.length == 3) {
            return Pair(search.second, fullSearch(search.first, 0, 7).first)
        } else {
            fullSearch(search.first, search.second, search.third)
        }
    } else {
        val search = search(s, from, to, 'L')
        fullSearch(search.first, search.second, search.third)
    }
}

fun toSeatId(row: Int, column: Int) = row * 8 + column

fun toSeatId(s: String): Int = fullSearch(s)
    .let { toSeatId(it.first, it.second) }

fun input() = "/five/input.txt".resourceAsText()
