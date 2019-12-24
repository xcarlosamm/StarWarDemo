package co.swapi.demo.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import co.swapi.demo.model.People
import co.swapi.demo.ui.view.PeopleViewHolder

/**
 * Adapter for the list of Star Wars films.
 */
class PeopleAdapter : ListAdapter<People, androidx.recyclerview.widget.RecyclerView.ViewHolder>(PEOPLE_ENTITY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        return PeopleViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            (holder as PeopleViewHolder).bind(data)
        }
    }

    companion object {
        private val PEOPLE_ENTITY_COMPARATOR = object : DiffUtil.ItemCallback<People>() {
            override fun areItemsTheSame(oldItem: People, newItem: People): Boolean = oldItem.characterId == newItem.characterId

            override fun areContentsTheSame(oldItem: People, newItem: People): Boolean = oldItem == newItem
        }
    }
}
