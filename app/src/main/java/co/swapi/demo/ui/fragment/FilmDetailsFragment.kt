package co.swapi.demo.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import co.swapi.demo.InjectionUtils
import co.swapi.demo.R
import co.swapi.demo.model.FilmWithCharacters
import co.swapi.demo.ui.adapter.CharactersAdapter
import co.swapi.demo.ui.viewmodel.FilmDetailsViewModel
import com.bumptech.glide.Glide

class FilmDetailsFragment : Fragment() {

    private val adapter = CharactersAdapter()
    private lateinit var viewModel: FilmDetailsViewModel
    val args : FilmDetailsFragmentArgs by navArgs()

    private lateinit var titleView: TextView
    private lateinit var openingCrawlView: TextView
    private lateinit var imageView: ImageView
    private lateinit var releaseYearView: TextView
    private lateinit var noResultsView: TextView
    private lateinit var charactersView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_film_details, container, false)

        imageView = root.findViewById(R.id.cover)
        titleView = root.findViewById(R.id.text_title)
        openingCrawlView = root.findViewById(R.id.text_opening_crawl)
        releaseYearView = root.findViewById(R.id.text_release_year)
        noResultsView = root.findViewById(R.id.empty_list)
        charactersView = root.findViewById(R.id.character_list)
        charactersView.adapter = adapter
        charactersView.visibility = View.GONE
        noResultsView.visibility = View.VISIBLE

        // get the view model
        viewModel = ViewModelProvider(this, InjectionUtils.provideFilmDetailsViewModelFactory(root.context))
            .get(FilmDetailsViewModel::class.java)

        Log.d("FilmDetailsFragment", "Loading filmWithCharacters details (id: ${args.filmId})")
        viewModel.filmWithCharacters.observe(viewLifecycleOwner, Observer<FilmWithCharacters> { bind(it) })
        viewModel.getFilm(args.filmId)

        return root
    }

    fun bind(filmWithCharacters: FilmWithCharacters?) {
        val film = filmWithCharacters!!.film

        val url = "https://picsum.photos/200/256?random=${args.filmId}"
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.ic_filmstrip_24dp)
            .into(imageView)

        titleView.text = film.title
        openingCrawlView.text = film.openingCrawl
        releaseYearView.text = film.year()

        adapter.submitList(filmWithCharacters.people)

        if (filmWithCharacters.people.isNotEmpty()) {
            charactersView.visibility = View.VISIBLE
            noResultsView.visibility = View.GONE
        }

        setActionBarTitle(film.title)
    }

    private fun setActionBarTitle(title: String?) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}