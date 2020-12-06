package extension

val dummy = object {}

fun String.getResourceAsText() = dummy.javaClass.getResource(this)
    .readText()
    .replace("\r\n", "\n")

fun String.splitByBlankLine() = this.split("\n\n")
