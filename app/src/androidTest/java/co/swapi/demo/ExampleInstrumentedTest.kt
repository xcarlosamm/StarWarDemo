package co.swapi.demo

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import co.swapi.demo.data.FilmWithCharactersRepository
import co.swapi.demo.nio.api.StarWarsAPIService
import co.swapi.demo.storage.AppDatabase
import co.swapi.demo.storage.AppLocalCache
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // assertEquals("co.swapi.demo", appContext.packageName)

        val service = StarWarsAPIService.getInstance()
        val cache = AppLocalCache(AppDatabase.getInstance(appContext), Executors.newSingleThreadExecutor())

        Log.i("cache", cache.filmDao.getAll().value?.toString() ?: "none")

        val repo = FilmWithCharactersRepository.getInstance(service, cache)
//        Log.i("cache", cache.filmWithCharactersDao.findById(1))

        assertEquals(2, 2)
    }
}
