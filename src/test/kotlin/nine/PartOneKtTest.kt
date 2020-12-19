package nine

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PartOneKtTest {

    @Test
    fun testSlice() {
        assertEquals(Pair(0, 11), nextSlice(Pair(0, 10)))
        assertEquals(Pair(1, 26), nextSlice(Pair(0, 25)))
        assertEquals(Pair(2, 27), nextSlice(Pair(1, 26)))
    }
}