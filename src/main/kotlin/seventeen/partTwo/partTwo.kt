package seventeen.partTwo

import extension.resourceAsText
import extension.splitByNewline

val input = "/seventeen/input.txt".resourceAsText().splitByNewline()
val testInput = "/seventeen/testInput.txt".resourceAsText().splitByNewline()

data class Coordinate(val x: Int, val y: Int, val z: Int, val w: Int)

fun Coordinate.surroundingCoordinates(): List<Coordinate> {
    return (x - 1..x + 1).flatMap { xCord ->
        (y - 1..y + 1).flatMap { yCord ->
            (z - 1..z + 1).flatMap { zCord ->
                (w - 1..w + 1).map { wCord ->
                    Coordinate(xCord, yCord, zCord, wCord)
                }
            }
        }
    }
        .filter { it != this }
}

data class Board(val coordinates: Map<Coordinate, Boolean>)

fun Board.countActiveSurroundingCoordinates(coordinate: Coordinate) = coordinate
    .surroundingCoordinates()
    .map { surroundingCoordinate -> coordinates.getOrDefault(surroundingCoordinate, false) }
    .count { it }

fun Board.step(): Board {
    val minCoord = getMinCoordinate()
    val maxCoord = getMaxCoordinate()

    val minCoordExtra = Coordinate(minCoord.x - 1, minCoord.y - 1, minCoord.z - 1, minCoord.w - 1)
    val maxCoordExtra = Coordinate(maxCoord.x + 1, maxCoord.y + 1, maxCoord.z + 1, maxCoord.w + 1)

    val next = coordinates.toMutableMap()
    allCoordinatesBetween(minCoordExtra, maxCoordExtra)
        .forEach { coordinate ->
            val value = coordinates.getOrDefault(coordinate, false)

            if (value) {
                when (countActiveSurroundingCoordinates(coordinate)) {
                    2, 3 -> next[coordinate] = true
                    else -> next[coordinate] = false
                }
            } else {
                when (countActiveSurroundingCoordinates(coordinate)) {
                    3 -> next[coordinate] = true
                    else -> next[coordinate] = false
                }
            }
        }

    return Board(next)
}

fun Board.getMinCoordinate() = sortedCoordinates().first()
fun Board.getMaxCoordinate() = sortedCoordinates().last()

fun Board.sortedCoordinates() = coordinates
    .map { it.key }
    .sortedWith(
        compareBy(
            { it.x },
            { it.y },
            { it.z },
            { it.w },
        )
    )

fun allCoordinatesBetween(from: Coordinate, to: Coordinate): List<Coordinate> {
    return (from.w..to.w).flatMap { w ->
        (from.z..to.z).flatMap { z ->
            (from.y..to.y).flatMap { y ->
                (from.x..to.x).map { x -> Coordinate(x, y, z, w) }
            }
        }
    }
}

fun Board.printBoard(): String {
    val minCoord = getMinCoordinate()
    val maxCoord = getMaxCoordinate()

    return (minCoord.w..maxCoord.w).joinToString("\n\n") { w ->
        (minCoord.z..maxCoord.z).joinToString("\n") { z ->
            val inner = (minCoord.y..maxCoord.y).joinToString("\n") { y ->
                (minCoord.x..maxCoord.x).map { x ->
                    if (coordinates.getOrDefault(Coordinate(x, y, z, w), false)) {
                        '#'
                    } else {
                        '.'
                    }
                }.joinToString("")
            }

            "\nz=$z, w=$w\n$inner"
        }
    }
}

fun Board.activeCoordinates() = coordinates.filter { it.value }.count()

fun runProgram(input: List<String>) {
    val parsedInput = input.mapIndexed { y, inputRow ->
        inputRow.mapIndexed { x, c ->
            Coordinate(x, y, 0, 0) to when (c) {
                '#' -> true
                '.' -> false
                else -> false
            }
        }
    }.flatten()
        .toMap()

    var currentBoard = Board(parsedInput)
    println(currentBoard.printBoard())

    for (cycle in 1..6) {
        println()
        println("After $cycle cycles:")
        println()
        currentBoard = currentBoard.step()
        println(currentBoard.printBoard())

        println()
        println("Active coordinates: ${currentBoard.activeCoordinates()}")
    }
}

fun main() = runProgram(input)
