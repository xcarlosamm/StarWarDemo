package co.swapi.demo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.swapi.demo.model.People
import co.swapi.demo.model.SearchResult
import co.swapi.demo.nio.api.StarWarsAPIService
import co.swapi.demo.nio.api.findPeopleById
import co.swapi.demo.nio.api.findPeopleByName
import co.swapi.demo.storage.AppLocalCache

/**
 * Repository class that works with the database and the web API service,
 * providing a unified data interface.
 */

class PeopleRepository(
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
    fun searchPeople(query: String): SearchResult<People> {
        Log.d("PeopleRepository", "New people query: $query")
        lastRequestedPage = 1
        requestAndSavePeople(query)

        // Get data from the local cache
        val data = cache.findPeopleByName(query)

        return SearchResult(data, errors)
    }

    fun nextPeople(query: String) {
        requestAndSavePeople(query)
    }

    private fun requestAndSavePeople(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        findPeopleByName(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { people ->
            cache.insertPeople(people) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            errors.postValue(error)
            isRequestInProgress = false
        })
    }

    fun getCharacter(id: Long): LiveData<People> {
        Log.d("PeopleRepository", "Find filmWithCharacters by : $id")

        findPeopleById(service, id, { people ->
            cache.insertPeople(listOf(people)) {
                // TODO:request and insert characters
                // continue here...
            }
        }, { error ->
            errors.postValue(error)
        })

        // Get data from the local cache
        return cache.findPeopleById(id)
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50

        // For Singleton instantiation
        @Volatile private var instance: PeopleRepository? = null

        fun getInstance(service: StarWarsAPIService, cache: AppLocalCache) =
            instance ?: synchronized(this) {
                instance ?: PeopleRepository(service, cache).also { instance = it }
            }
    }
}