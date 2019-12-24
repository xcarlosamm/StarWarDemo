package co.swapi.demo.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import co.swapi.demo.model.Film
import co.swapi.demo.model.SearchResult
import co.swapi.demo.nio.api.StarWarsAPIService
import co.swapi.demo.nio.api.findFilmsByName
import co.swapi.demo.storage.AppLocalCache

/**
 * Repository class that works with the database and the web API service,
 * providing a unified data interface.
 */

class FilmsRepository(
    private val service: StarWarsAPIService,
    private val cache: AppLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    // LiveData of network errors.
    private val errors = MutableLiveData<String>()

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Search films whose names match the query.
     */
    fun searchFilm(query: String): SearchResult<Film> {
        Log.d("FilmsRepository", "New filmWithCharacters query: $query")
        lastRequestedPage = 1
        requestAndSaveFilms(query)

        // Get data from the local cache
        val data = cache.findFilmsByName(query)

        return SearchResult(data, errors)
    }

    fun nextFilms(query: String) {
        requestAndSaveFilms(query)
    }

    private fun requestAndSaveFilms(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        findFilmsByName(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { films ->
            cache.insertFilms(films) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            errors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50

        // For Singleton instantiation
        @Volatile private var instance: FilmsRepository? = null

        fun getInstance(service: StarWarsAPIService, cache: AppLocalCache) =
            instance ?: synchronized(this) {
                instance ?: FilmsRepository(service, cache).also { instance = it }
            }
    }
}