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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.swapi.demo.R
import co.swapi.demo.model.People
import co.swapi.demo.ui.fragment.PeopleFragmentDirections
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * View Holder for a [People] RecyclerView list item.
 */
open class PeopleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val image: ImageView = view.findViewById(R.id.image)
    private val name: TextView = view.findViewById(R.id.name)
    private val gender: TextView = view.findViewById(R.id.gender)
    private val birthYear: TextView = view.findViewById(R.id.birth_year)

    private var people: People? = null
    private var context: Context = view.context

    init {
        view.setOnClickListener {
            view.findNavController().navigate(gotoDetailsAction(people!!.characterId))
        }
    }

    open fun gotoDetailsAction(id: Long):NavDirections {
        return PeopleFragmentDirections.actionGotoPeopleDetails(id)
    }

    fun bind(data: People?) {
        if (data == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            image.visibility = View.GONE
            gender.visibility = View.GONE
            birthYear.visibility = View.GONE
        } else {
            this.people = data
            name.text = data.name?.split(' ')?.joinToString(" ") { it.capitalize() }
            if (data.gender !in listOf("n/a", "unknown", "none")) {
                gender.text = data.gender?.capitalize()
            } else {
                gender.text = context.getString(R.string.unknown_gender)
            }

            if (data.birthYear !in listOf("n/a", "unknown", "none")) {
                birthYear.text = data.birthYear
            } else {
                birthYear.text = context.getString(R.string.unknown_birth_year)
            }

            val id = data.characterId
            val url = "https://picsum.photos/200/200?random=1$id"
            Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_account_24dp)
                .into(image)

            image.visibility = View.VISIBLE
            gender.visibility = View.VISIBLE
            birthYear.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup): PeopleViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.people_view_item, parent, false)
            return PeopleViewHolder(view)
        }
    }
}
