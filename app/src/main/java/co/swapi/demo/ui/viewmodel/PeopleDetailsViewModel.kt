package co.swapi.demo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.swapi.demo.data.PeopleRepository
import co.swapi.demo.model.People

class PeopleDetailsViewModel(private val repository: PeopleRepository) : ViewModel() {

    private val characterId = MutableLiveData<Long>()

    val people: LiveData<People> = Transformations.switchMap(characterId) {
        repository.getCharacter(it)
    }
//    val error: LiveData<String> = Transformations.switchMap(results) { it.errors }

    /**
     * Gets the filmWithCharacters based on the id.
     */
    fun getPeople(id: Long) {
        characterId.postValue(id)
    }

    /**
     * Get the last filmId value.
     */
    fun currentCharacterId(): Long? = characterId.value
}
