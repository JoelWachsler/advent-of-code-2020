package twelve.partTwo

import extension.resourceAsText
import extension.splitByNewline
import twelve.partTwo.CardinalDirection.*
import twelve.partTwo.Degrees.*
import twelve.partTwo.RotationDirection.*
import java.lang.Math.toRadians
import kotlin.math.*

val input = "/twelve/input.txt".resourceAsText().splitByNewline()
val testInput = "/twelve/testInput.txt".resourceAsText().splitByNewline()

data class Coordinate(val east: Long, val north: Long)

fun Coordinate.north(steps: Long) = copy(north = north + steps)
fun Coordinate.west(steps: Long) = copy(east = east - steps)
fun Coordinate.south(steps: Long) = copy(north = north - steps)
fun Coordinate.east(steps: Long) = copy(east = east + steps)
fun Coordinate.add(coordinate: Coordinate) = Coordinate(east + coordinate.east, north + coordinate.north)
fun Coordinate.inverse() = Coordinate(-east, -north)
fun Coordinate.subtract(coordinate: Coordinate) = add(coordinate.inverse())
fun Coordinate.distance(coordinate: Coordinate) =
    (coordinate.east - east).absoluteValue + (coordinate.north - north).absoluteValue

fun Coordinate.rotate(angle: Double): Coordinate {
    val s = sin(toRadians(angle))
    val c = cos(toRadians(angle))

    // rotate
    return Coordinate(
        (east * c - north * s).roundToLong(),
        (east * s + north * c).roundToLong(),
    )
}

enum class Degrees {
    DEG90,
    DEG180,
    DEG270,
}

fun parseDegrees(value: Long): Degrees = when (value) {
    90L -> DEG90
    180L -> DEG180
    270L -> DEG270
    else -> throw Error("Invalid...")
}

enum class CardinalDirection {
    NORTH,
    WEST,
    SOUTH,
    EAST,
}

fun parseCardinalDirection(char: String): CardinalDirection? = when (char) {
    "N" -> NORTH
    "S" -> SOUTH
    "E" -> EAST
    "W" -> WEST
    else -> null
}

enum class RotationDirection {
    LEFT,
    RIGHT,
    FORWARD,
}

fun parseRotationDirection(char: String): RotationDirection? = when (char) {
    "L" -> LEFT
    "R" -> RIGHT
    "F" -> FORWARD
    else -> null
}

sealed class Command {
    abstract fun applyToState(state: State): State
}

data class CardinalCommand(val cardinalDirection: CardinalDirection, val amount: Long) : Command() {

    override fun applyToState(state: State): State {
        return when (cardinalDirection) {
            NORTH -> state.copy(waypoint = state.waypoint.north(amount))
            WEST -> state.copy(waypoint = state.waypoint.west(amount))
            SOUTH -> state.copy(waypoint = state.waypoint.south(amount))
            EAST -> state.copy(waypoint = state.waypoint.east(amount))
        }
    }
}

data class RotationalCommand(val rotationDirection: RotationDirection) : Command() {

    override fun applyToState(state: State): State {
        return when (rotationDirection) {
            LEFT -> state.copy(waypoint = state.waypoint.rotate(90.0))
            RIGHT -> state.copy(waypoint = state.waypoint.rotate(-90.0))
            FORWARD -> throw error("Invalid")
        }
    }
}

data class ForwardCommand(val amount: Long) : Command() {

    override fun applyToState(state: State) = state.copy(
        coordinate = Coordinate(
            state.coordinate.east + state.waypoint.east * amount,
            state.coordinate.north + state.waypoint.north * amount,
        )
    )
}

fun parseCommand(char: String, amount: Long): List<Command> {
    val card = parseCardinalDirection(char)
    if (card != null) {
        return listOf(CardinalCommand(card, amount))
    }

    val rot = parseRotationDirection(char)
    if (rot != null) {
        val rotCommand = RotationalCommand(rot)

        if (rot == FORWARD) {
            return listOf(ForwardCommand(amount))
        }

        return when (parseDegrees(amount)) {
            DEG90 -> listOf(rotCommand)
            DEG180 -> listOf(rotCommand, rotCommand)
            DEG270 -> listOf(rotCommand, rotCommand, rotCommand)
        }
    }

    throw Error("Invalid...")
}

fun parseInput(line: String): List<Command> {
    val parseRegex = Regex("(\\w)(\\d+)")
    val regexResult = parseRegex.findAll(line).toList()
        .flatMap { it.groups }
        .mapNotNull { it }

    val amount = regexResult[2].value.toLong()

    return parseCommand(regexResult[1].value, amount)
}

fun parseInput(lines: List<String>) = lines
    .map { parseInput(it) }
    .flatten()

data class State(val coordinate: Coordinate, val waypoint: Coordinate)

fun runPartTwo(input: List<String>) {
    val commands = parseInput(input)

    val initialState = State(Coordinate(0, 0), Coordinate(10, 1))
    var currentState = initialState
    println("Before commands")
    println(currentState)
    println()

    for (command in commands) {
        currentState = command.applyToState(currentState)
        println(command)
        println(currentState)
        println()
    }

    println("After commands")
    println(currentState)

    val distance = initialState.coordinate.distance(currentState.coordinate)
    println("Distance is: $distance")
}

fun main() = runPartTwo(input)