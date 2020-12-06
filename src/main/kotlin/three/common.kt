package three

import extension.resourceAsText
import extension.splitByNewline

fun patternGenerator(pattern: String, itemAtIndex: Int): String {
    val patternLength = pattern.length - 1
    if (itemAtIndex > patternLength) {
        return patternGenerator(pattern + pattern, itemAtIndex)
    }

    return pattern[itemAtIndex].toString()
}

fun findTreesHit(field: List<String>, xStep: Int, yStep: Int): Int {
    var treeHit = 0
    var x = 0
    var y = 0

    while (y < field.size) {
        if (patternGenerator(field[y], x) == "#") {
            treeHit++
        }

        x += xStep
        y += yStep
    }

    return treeHit
}

fun input() = "/three/input.txt".resourceAsText().splitByNewline()
