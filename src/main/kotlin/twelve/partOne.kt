package twelve

import extension.resourceAsText
import extension.splitByNewline
import twelve.CardinalDirection.*
import twelve.Degrees.*
import twelve.RotationDirection.*
import kotlin.math.absoluteValue

val input = "/twelve/input.txt".resourceAsText().splitByNewline()
val testInput = "/twelve/testInput.txt".resourceAsText().splitByNewline()

data class Coordinate(val east: Int, val north: Int)

fun Coordinate.north(steps: Int) = Coordinate(east, north + steps)
fun Coordinate.west(steps: Int) = Coordinate(east - steps, north)
fun Coordinate.south(steps: Int) = Coordinate(east, north - steps)
fun Coordinate.east(steps: Int) = Coordinate(east + steps, north)
fun Coordinate.distance(coordinate: Coordinate) =
    (coordinate.east - east).absoluteValue + (coordinate.north - north).absoluteValue

enum class Degrees {
    DEG90,
    DEG180,
    DEG270,
}

fun parseDegrees(value: Int): Degrees = when (value) {
    90 -> DEG90
    180 -> DEG180
    270 -> DEG270
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

data class CardinalCommand(val cardinalDirection: CardinalDirection, val amount: Int) : Command() {

    override fun applyToState(state: State): State {
        return when (cardinalDirection) {
            NORTH -> state.copy(coordinate = state.coordinate.north(amount))
            WEST -> state.copy(coordinate = state.coordinate.west(amount))
            SOUTH -> state.copy(coordinate = state.coordinate.south(amount))
            EAST -> state.copy(coordinate = state.coordinate.east(amount))
        }
    }
}

data class RotationalCommand(val rotationDirection: RotationDirection) : Command() {

    private fun applyRotation(direction: CardinalDirection): CardinalDirection {
        return when (direction) {
            NORTH -> when (rotationDirection) {
                LEFT -> WEST
                RIGHT -> EAST
                FORWARD -> NORTH
            }
            WEST -> when (rotationDirection) {
                LEFT -> SOUTH
                RIGHT -> NORTH
                FORWARD -> WEST
            }
            SOUTH -> when (rotationDirection) {
                LEFT -> EAST
                RIGHT -> WEST
                FORWARD -> SOUTH
            }
            EAST -> when (rotationDirection) {
                LEFT -> NORTH
                RIGHT -> SOUTH
                FORWARD -> EAST
            }
        }
    }

    override fun applyToState(state: State) = state.copy(cardinalDirection = applyRotation(state.cardinalDirection))
}

data class ForwardCommand(val amount: Int) : Command() {

    private fun forward(cardinalDirection: CardinalDirection, inputCoordinate: Coordinate): Coordinate =
        when (cardinalDirection) {
            NORTH -> inputCoordinate.north(amount)
            WEST -> inputCoordinate.west(amount)
            SOUTH -> inputCoordinate.south(amount)
            EAST -> inputCoordinate.east(amount)
        }

    override fun applyToState(state: State) =
        state.copy(coordinate = forward(state.cardinalDirection, state.coordinate))
}

fun parseCommand(char: String, amount: Int): List<Command> {
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

    val amount = regexResult[2].value.toInt()

    return parseCommand(regexResult[1].value, amount)
}

fun parseInput(lines: List<String>) = lines
    .map { parseInput(it) }
    .flatten()

data class State(val coordinate: Coordinate, val cardinalDirection: CardinalDirection)

fun runPartOne(input: List<String>) {
    val commands = parseInput(input)

    val initialState = State(Coordinate(0, 0), EAST)
    var currentState = initialState
    println("Before commands")
    println(currentState)
    println()

    for (command in commands) {
        currentState = command.applyToState(currentState)
        println("Current state is after command")
        println(command)
        println(currentState)
        println()
    }

    println("After commands")
    println(currentState)

    val distance = initialState.coordinate.distance(currentState.coordinate)
    println("Distance is: $distance")
}

fun main() {
    runPartOne(input)
}