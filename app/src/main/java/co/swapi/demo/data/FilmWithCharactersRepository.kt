package co.swapi.demo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import co.swapi.demo.model.FilmWithCharacters
import co.swapi.demo.nio.api.StarWarsAPIService
import co.swapi.demo.nio.api.findFilmById
import co.swapi.demo.nio.api.findPeopleById
import co.swapi.demo.storage.AppLocalCache

/**
 * Repository class that works with the database and the web API service,
 * providing a unified data interface.
 */

class FilmWithCharactersRepository(
    private val service: StarWarsAPIService,
    private val cache: AppLocalCache
) {

    // LiveData of network errors.
    private val errors = MutableLiveData<String>()

    /**
     * Note: Start Wars API lacks, request the film and each of it characters one-by-one
     * */
    fun getFilm(id: Long): LiveData<FilmWithCharacters> {
        Log.d("FilmsRepository", "Find filmWithCharacters by : $id")

        findFilmById(service, id, { film, characterIds ->
            // Persist film
            cache.insertFilms(listOf(film)) {

                // Request and insert characters
                characterIds.forEach {
                    findPeopleById(service, it, { people ->
                        cache.insertPeople(listOf(people)) {
                            // Bind data into local database
                            cache.insertCharacters(film, listOf(people)) {}
                        }
                    }, { error ->
                        errors.postValue(error)
                    })
                }
            }
        }, { error ->
            errors.postValue(error)
        })

        // Get data from the local cache
        return cache.findFilmWithCharactersById(id)
    }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: FilmWithCharactersRepository? = null

        fun getInstance(service: StarWarsAPIService, cache: AppLocalCache) =
            instance ?: synchronized(this) {
                instance ?: FilmWithCharactersRepository(service, cache).also { instance = it }
            }
    }
}