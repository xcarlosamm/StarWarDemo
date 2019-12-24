package co.swapi.demo.storage

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import co.swapi.demo.model.CharactersCrossRef
import co.swapi.demo.model.Film
import co.swapi.demo.model.FilmWithCharacters
import co.swapi.demo.model.People
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class AppLocalCache(
    appDatabase: AppDatabase,
    private val ioExecutor: Executor
) {

    var filmDao = appDatabase.filmDao()
    var filmWithCharactersDao = appDatabase.filmWithCharactersDao()
    var peopleDao = appDatabase.peopleDao()

    fun findFilmWithCharactersById(id: Long):LiveData<FilmWithCharacters> {
        return filmWithCharactersDao.findById(id)
    }

    fun findFilmById(id: Long):LiveData<Film> {
        return filmDao.findById(id)
    }

    /**
     * Insert a list of items in the database, on a background thread.
     */
    fun insertFilms(films: List<Film>, insertFinished: () -> Unit) {

        ioExecutor.execute {
            Log.d("AppLocalCache", "Inserting films ${films.size} rows")
            filmDao.insertAll(films)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<Film>> from the Dao, based on a [query]. If the name contains
     * multiple words separated by spaces, then we're emulating the API behavior and allow
     * any characters between the words.
     * @param query filmWithCharacters title
     */
    fun findFilmsByName(query: String): LiveData<List<Film>> {
        return if (TextUtils.isEmpty(query)) {
            filmDao.getAll()
        } else {
            // appending '%' so we can allow other characters to be before and after the query string
            val q = "%${query.replace(' ', '%')}%"
            filmDao.findByName(q)
        }
    }

    fun findPeopleById(id: Long):LiveData<People> {
        return peopleDao.findById(id)
    }

    fun insertPeople(people: List<People>, insertFinished: () -> Unit) {

        ioExecutor.execute {
            Log.d("AppLocalCache", "Inserting people ${people.size} rows")
            peopleDao.insertAll(people)
            insertFinished()
        }
    }

    fun insertCharacters(film: Film, people: List<People>, insertFinished: () -> Unit) {

        ioExecutor.execute {
            Log.d("AppLocalCache", "Film \"${film.title}\". Inserting characters ${people.size} rows")
            filmWithCharactersDao.insertAll(people.map { CharactersCrossRef(film.filmId, it.characterId) })
            insertFinished()
        }
    }

    fun findPeopleByName(query: String): LiveData<List<People>> {
        return if (TextUtils.isEmpty(query)) {
            peopleDao.getAll()
        } else {
            // appending '%' so we can allow other characters to be before and after the query string
            val q = "%${query.replace(' ', '%')}%"
            peopleDao.findByName(q)
        }
    }
}
