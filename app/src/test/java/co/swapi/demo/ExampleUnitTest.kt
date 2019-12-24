package co.swapi.demo

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    @Test
    fun regex_isCorrect() {

        val url = "https://swapi.co/api/films/2/31/"
        assertEquals(Regex("\\d+").findAll(url).last().value.toInt(), 31)
    }
}
