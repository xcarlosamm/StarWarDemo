package co.swapi.demo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.swapi.demo.data.FilmWithCharactersRepository
import co.swapi.demo.model.FilmWithCharacters

class FilmDetailsViewModel(private val repository: FilmWithCharactersRepository) : ViewModel() {

    private val filmId = MutableLiveData<Long>()
    private val characterIds = MutableLiveData<List<Long>>()

    val filmWithCharacters: LiveData<FilmWithCharacters> = Transformations.switchMap(filmId) {
        repository.getFilm(it)
    }
//    val error: LiveData<String> = Transformations.switchMap(results) { it.errors }

    /**
     * Gets the filmWithCharacters based on the id.
     */
    fun getFilm(id: Long) {
        filmId.postValue(id)
    }

    /**
     * Get the last filmId value.
     */
    fun currentFilmId(): Long? = filmId.value
}
