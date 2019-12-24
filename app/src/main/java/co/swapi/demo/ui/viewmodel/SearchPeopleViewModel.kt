/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.swapi.demo.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import co.swapi.demo.data.PeopleRepository
import co.swapi.demo.model.People
import co.swapi.demo.model.SearchResult

/**
 * ViewModel for the [FilmListFragment] screen.
 * The ViewModel works with the [FilmsRepository] to get the data.
 */
class SearchPeopleViewModel(private val repository: PeopleRepository) : ViewModel() {

    companion object {
        private const val VISIBLE_THRESHOLD = 5
    }

    private val queryLiveData = MutableLiveData<String>()
    private val result: LiveData<SearchResult<People>> = Transformations.map(queryLiveData) {
        repository.searchPeople(it)
    }

    val people: LiveData<List<People>> = Transformations.switchMap(result) { it.data }
    val errors: LiveData<String> = Transformations.switchMap(result) { it.errors }

    /**
     * Search the Star Wars films based on a query string.
     */
    fun search(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun listScrolled(visibleItemCount: Int, lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount) {
            val immutableQuery = lastQueryValue()
            if (immutableQuery != null) {
                repository.nextPeople(immutableQuery)
            }
        }
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value
}
