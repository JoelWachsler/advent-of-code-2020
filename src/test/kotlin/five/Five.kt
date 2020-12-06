package five

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested

internal class Five {

    @Nested
    inner class PartOne {

        @Test
        fun first() {
            assertEquals(357, partOneTest("FBFBBFFRLR"));
        }

        @Test
        fun second() {
            assertEquals(567, partOneTest("BFFFBBFRRR"));
        }

        @Test
        fun third() {
            assertEquals(119, partOneTest("FFFBBBFRRR"));
        }

        @Test
        fun fourth() {
            assertEquals(820, partOneTest("BBFFBBFRLL"));
        }
    }

    @Nested
    inner class Search {

        @Test
        fun searchUpper() {
            assertEquals(Triple("BFBBFFRLR", 0, 63), search("FBFBBFFRLR", 0, 127))
        }

        @Test
        fun searchLower() {
            assertEquals(Triple("FBBFFRLR", 32, 63), search("BFBBFFRLR", 0, 63))
        }

        @Test
        fun searchLowerAgain() {
            assertEquals(Triple("BBFFRLR", 32, 47), search("FBBFFRLR", 32, 63))
        }

        @Test
        fun searchUpperAgain() {
            assertEquals(Triple("BFFRLR", 40, 47), search("BBFFRLR", 32, 47))
        }

        @Test
        fun searchUpperAgainAgain() {
            assertEquals(Triple("FFRLR", 44, 47), search("BFFRLR", 40, 47))
        }

        @Test
        fun searchLowerAgainAgain() {
            assertEquals(Triple("FRLR", 44, 45), search("FFRLR", 44, 47))
        }

        @Test
        fun searchLowerAgainAgainAgain() {
            assertEquals(Triple("RLR", 44, 44), search("FRLR", 44, 45))
        }

        @Test
        fun searchFinalPartStepOne() {
            assertEquals(Triple("LR", 4, 7), search("RLR", 0, 7, 'L'))
        }

        @Test
        fun searchFinalPartStepTwo() {
            assertEquals(Triple("R", 4, 5), search("LR", 4, 7, 'L'))
        }

        @Test
        fun searchFinalPartStepThree() {
            assertEquals(Triple("", 5, 5), search("R", 4, 5, 'L'))
        }
    }
}