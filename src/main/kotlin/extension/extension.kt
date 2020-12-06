package extension

val dummy = object {}

fun String.resourceAsText() = dummy.javaClass.getResource(this)
    .readText()
    .replace("\r\n", "\n")

fun String.splitByBlankLine() = this.split("\n\n")
