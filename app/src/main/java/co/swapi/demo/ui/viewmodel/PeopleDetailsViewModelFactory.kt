package co.swapi.demo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.swapi.demo.data.PeopleRepository

/**
 * Factory for ViewModels
 */
class PeopleDetailsViewModelFactory(private val repository: PeopleRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PeopleDetailsViewModel(repository) as T
    }
}