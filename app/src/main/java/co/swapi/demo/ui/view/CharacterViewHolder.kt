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

package co.swapi.demo.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDirections
import co.swapi.demo.R
import co.swapi.demo.model.People
import co.swapi.demo.ui.fragment.FilmDetailsFragmentDirections

/**
 * View Holder for a [People] RecyclerView list item.
 */
class CharacterViewHolder(view: View) : PeopleViewHolder(view) {

    override fun gotoDetailsAction(id: Long):NavDirections {
        return FilmDetailsFragmentDirections.actionCharactersListGotoPeopleDetails(id)
    }

    companion object {
        fun create(parent: ViewGroup): CharacterViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.people_view_item, parent, false)
            return CharacterViewHolder(view)
        }
    }
}
