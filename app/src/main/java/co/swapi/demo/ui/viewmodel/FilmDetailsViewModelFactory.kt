package co.swapi.demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.swapi.demo.data.FilmWithCharactersRepository

/**
 * Factory for ViewModels
 */
class FilmDetailsViewModelFactory(private val repository: FilmWithCharactersRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FilmDetailsViewModel(repository) as T
    }
}