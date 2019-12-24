package co.swapi.demo.model

import androidx.lifecycle.LiveData

/**
 * SearchResult from a searchFilm, which contains LiveData<List<T>> holding query data,
 * and a LiveData<String> of network error state.
 */
data class SearchResult<T>(
    val data: LiveData<List<T>>,
    val errors: LiveData<String>
)