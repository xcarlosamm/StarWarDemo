package co.swapi.demo.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.swapi.demo.model.Film
import co.swapi.demo.ui.view.FilmViewHolder

/**
 * Adapter for the list of Star Wars films.
 */
class FilmsAdapter : ListAdapter<Film, androidx.recyclerview.widget.RecyclerView.ViewHolder>(FILM_ENTITY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return FilmViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        
        if (data != null) {
            (holder as FilmViewHolder).bind(data)
        }
    }

    companion object {
        private val FILM_ENTITY_COMPARATOR = object : DiffUtil.ItemCallback<Film>() {
            override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem.filmId == newItem.filmId

            override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean = oldItem == newItem
        }
    }
}
