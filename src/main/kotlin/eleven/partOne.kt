package eleven

import extension.resourceAsText
import extension.splitByNewline
import java.lang.Error

data class Coordinate(val x: Int, val y: Int)

fun Coordinate.topLeft() = Coordinate(x - 1, y - 1)
fun Coordinate.left() = Coordinate(x - 1, y)
fun Coordinate.bottomLeft() = Coordinate(x - 1, y + 1)
fun Coordinate.bottom() = Coordinate(x, y + 1)
fun Coordinate.bottomRight() = Coordinate(x + 1, y + 1)
fun Coordinate.right() = Coordinate(x + 1, y)
fun Coordinate.topRight() = Coordinate(x + 1, y - 1)
fun Coordinate.top() = Coordinate(x, y - 1)

class PartOne {

    enum class Spot(val char: Char) {
        FLOOR('.'),
        EMPTY('L'),
        OCCUPIED('#');

        override fun toString(): String {
            return "$char"
        }
    }

    fun parsePlayingField(fieldInput: List<String>): Board {
        val field = fieldInput.map { row ->
            row.map { spot ->
                when (spot) {
                    '.' -> {
                        Spot.FLOOR
                    }
                    'L' -> {
                        Spot.EMPTY
                    }
                    '#' -> {
                        Spot.OCCUPIED
                    }
                    else -> {
                        throw Error("Invalid: $spot")
                    }
                }
            }.toMutableList()
        }.toMutableList()

        return Board(field)
    }

    class Board(private val field: MutableList<MutableList<Spot>>) {

        private fun cloneBoard(): Board = Board(field.map { outer -> outer.toMutableList() }.toMutableList())

        fun step(): Board {
            val nextBoard = cloneBoard()
            getCoordinatesWithoutFloor().forEach { coordinate ->
                val coordinateValue = getCoordinateValue(coordinate)!!
                val grouping = getAdjacentValuesGrouped(coordinate)

                if (coordinateValue == Spot.EMPTY && grouping.getOrDefault(Spot.OCCUPIED, 0) == 0) {
                    nextBoard.setCoordinateValue(coordinate, Spot.OCCUPIED)
                } else if (coordinateValue == Spot.OCCUPIED && grouping.getOrDefault(Spot.OCCUPIED, 0) >= 4) {
                    nextBoard.setCoordinateValue(coordinate, Spot.EMPTY)
                }
            }

            return nextBoard
        }

        private fun getCoordinatesWithoutFloor(): List<Coordinate> = getCoordinates().filter {
            val value = getCoordinateValue(it) ?: throw Error("Failed on: $it")
            value != Spot.FLOOR
        }

        private fun getCoordinates(): List<Coordinate> = field
            .mapIndexed { yCoordinate, inner ->
                inner.mapIndexed { xCoordinate, _ ->
                    Coordinate(
                        xCoordinate,
                        yCoordinate,
                    )
                }
            }
            .flatten()

        private fun setCoordinateValue(coordinate: Coordinate, newValue: Spot) {
            field[coordinate.y][coordinate.x] = newValue
        }

        private fun getCoordinateValue(coordinate: Coordinate): Spot? {
            if (coordinate.x < 0 || coordinate.y < 0) {
                return null
            }

            val y = field.getOrNull(coordinate.y)
            if (y != null) {
                val x = y.getOrNull(coordinate.x)
                if (x != null) {
                    return x
                }
            }
            return null
        }

        private fun getAdjacentCoordinates(coordinate: Coordinate) = listOf(
            coordinate.topLeft(),
            coordinate.left(),
            coordinate.bottomLeft(),
            coordinate.bottom(),
            coordinate.bottomRight(),
            coordinate.right(),
            coordinate.topRight(),
            coordinate.top(),
        )

        private fun getAdjacentValues(coordinate: Coordinate): List<Spot> = getAdjacentCoordinates(coordinate)
            .mapNotNull { getCoordinateValue(it) }

        private fun getAdjacentValuesGrouped(coordinate: Coordinate): Map<Spot, Int> = getAdjacentValues(coordinate)
            .groupBy { it }
            .map { it.key to it.value.size }
            .associateBy({ it.first }, { it.second })

        fun getAllValuesGrouped(): Map<Spot, Int> = getCoordinatesWithoutFloor()
            .asSequence()
            .mapNotNull { coordinate -> getCoordinateValue(coordinate) }
            .groupBy { it }
            .map { it.key to it.value.size }
            .associateBy({ it.first }, { it.second })

        override fun toString(): String {
            return field
                .joinToString("\n")
                { it.joinToString(" ") }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Board

            if (field != other.field) return false

            return true
        }

        override fun hashCode(): Int {
            return field.hashCode()
        }
    }

    fun runProgram(fieldInput: List<String>) {
        val board = parsePlayingField(fieldInput)
        println(board)
        var currentBoard = board
        while (true) {
            val nextBoard = currentBoard.step()
            if (currentBoard == nextBoard) {
                break
            }
            currentBoard = nextBoard
        }

        println("The final board is")
        println(currentBoard)
        val grouped = currentBoard.getAllValuesGrouped()
        println("There are ${grouped[Spot.OCCUPIED]} occupied seats")
    }

}

fun main() {
    val testInput = "/eleven/testInput.txt".resourceAsText().splitByNewline()
    val input = "/eleven/input.txt".resourceAsText().splitByNewline()

    PartOne().runProgram(input)
}
