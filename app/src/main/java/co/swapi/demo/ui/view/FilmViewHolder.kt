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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import co.swapi.demo.R
import co.swapi.demo.model.Film
import co.swapi.demo.ui.fragment.FilmListFragmentDirections
import com.bumptech.glide.Glide


/**
 * View Holder for a [Film] RecyclerView list item.
 */
class FilmViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val cover: ImageView = view.findViewById(R.id.cover)
    private val title: TextView = view.findViewById(R.id.title)
    private val openingCrawl: TextView = view.findViewById(R.id.description)
    private val releaseYear: TextView = view.findViewById(R.id.release_year)

    private var film: Film? = null
    private var context: Context = view.context

    init {
        view.setOnClickListener {
            val action = FilmListFragmentDirections.actionGotoNavigationFilmDetails(film!!.filmId)
            view.findNavController().navigate(action)
        }
    }

    fun bind(data: Film?) {
        if (data == null) {
            val resources = itemView.resources
            title.text = resources.getString(R.string.loading)
            cover.visibility = View.GONE
            openingCrawl.visibility = View.GONE
            releaseYear.visibility = View.GONE
        } else {
            this.film = data
            title.text = data.title
            openingCrawl.text = data.openingCrawl
            releaseYear.text = data.year()

            val id = data.filmId
            val url = "https://picsum.photos/200/256?random=$id"
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.ic_filmstrip_24dp)
                .into(cover)

            cover.visibility = View.VISIBLE
            openingCrawl.visibility = View.VISIBLE
            releaseYear.visibility = View.VISIBLE
        }
    }

    companion object {
        fun create(parent: ViewGroup): FilmViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.film_view_item, parent, false)
            return FilmViewHolder(view)
        }
    }
}
