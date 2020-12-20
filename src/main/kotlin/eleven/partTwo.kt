package eleven

import extension.resourceAsText
import extension.splitByNewline

fun Coordinate.isPositive() = x >= 0 && y >= 0
infix fun Coordinate.lessOrEqualTo(coordinate: Coordinate) = x <= coordinate.x && y <= coordinate.y

fun Coordinate.isValid(bottomRight: Coordinate) = isPositive() && this lessOrEqualTo bottomRight

fun Coordinate.traceTopLeft(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.topLeft()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.topLeft()
    }
    return coordinates
}

fun Coordinate.traceLeft(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.left()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.left()
    }
    return coordinates
}

fun Coordinate.traceBottomLeft(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.bottomLeft()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.bottomLeft()
    }
    return coordinates
}

fun Coordinate.traceBottom(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.bottom()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.bottom()
    }
    return coordinates
}

fun Coordinate.traceBottomRight(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.bottomRight()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.bottomRight()
    }
    return coordinates
}

fun Coordinate.traceRight(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.right()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.right()
    }
    return coordinates
}
fun Coordinate.traceTopRight(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.topRight()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.topRight()
    }
    return coordinates
}

fun Coordinate.traceTop(bottomRight: Coordinate): List<Coordinate> {
    val coordinates = mutableListOf<Coordinate>()
    var currentCoordinate = this.top()
    while (currentCoordinate.isValid(bottomRight)) {
        coordinates.add(currentCoordinate)
        currentCoordinate = currentCoordinate.top()
    }
    return coordinates
}

class PartTwoBoard(field: MutableList<MutableList<Spot>>) : Board(field) {

    override fun cloneBoard(): PartTwoBoard = PartTwoBoard(super.cloneBoard().field)

    override fun step(): PartTwoBoard {
        val nextBoard = cloneBoard()

        getCoordinatesWithoutFloor().forEach { coordinate ->
            val coordinateValue = getCoordinateValue(coordinate)!!
            val grouping = getTracedValues(coordinate)

            if (coordinateValue == Spot.EMPTY && grouping.getOrDefault(Spot.OCCUPIED, 0) == 0) {
                nextBoard.setCoordinateValue(coordinate, Spot.OCCUPIED)
            } else if (coordinateValue == Spot.OCCUPIED && grouping.getOrDefault(Spot.OCCUPIED, 0) >= 5) {
                nextBoard.setCoordinateValue(coordinate, Spot.EMPTY)
            }
        }

        return nextBoard
    }

    fun bottomRightCoordinate() = Coordinate(field.last().lastIndex, field.lastIndex)

    fun traceCoordinate(coordinate: Coordinate) = listOf(
        coordinate.traceTopLeft(bottomRightCoordinate()),
        coordinate.traceLeft(bottomRightCoordinate()),
        coordinate.traceBottomLeft(bottomRightCoordinate()),
        coordinate.traceBottom(bottomRightCoordinate()),
        coordinate.traceBottomRight(bottomRightCoordinate()),
        coordinate.traceRight(bottomRightCoordinate()),
        coordinate.traceTopRight(bottomRightCoordinate()),
        coordinate.traceTop(bottomRightCoordinate()),
    )

    fun tracedCoordinateWithoutFloor(coordinate: Coordinate) = traceCoordinate(coordinate)
        .map { trace -> trace.filter { getCoordinateValue(it)!! != Spot.FLOOR } }

    fun getTracedValues(forCoordinate: Coordinate) = tracedCoordinateWithoutFloor(forCoordinate)
        .asSequence()
        .mapNotNull { it.firstOrNull() }
        .mapNotNull { getCoordinateValue(it) }
        .groupBy { it }
        .map { it.key to it.value.size }
        .associateBy({ it.first }, { it.second })
}

fun runProgram2(fieldInput: List<String>) {
    val board = PartTwoBoard(parsePlayingField(fieldInput))
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

fun main() {
    val testInput = "/eleven/testInput.txt".resourceAsText().splitByNewline()
    val input = "/eleven/input.txt".resourceAsText().splitByNewline()

    runProgram2(input)
}
