package co.swapi.demo.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import co.swapi.demo.InjectionUtils
import co.swapi.demo.R
import co.swapi.demo.model.Film
import co.swapi.demo.ui.adapter.FilmsAdapter
import co.swapi.demo.ui.viewmodel.SearchFilmsViewModel
import com.google.android.material.textfield.TextInputEditText

class FilmListFragment : Fragment() {

    private lateinit var viewModel: SearchFilmsViewModel
    private val adapter = FilmsAdapter()

    private lateinit var filmList: RecyclerView
    private lateinit var searchInput: TextInputEditText
    private lateinit var emptyList: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_film_list, container, false)

        // get the view model
        viewModel = ViewModelProvider(this, InjectionUtils.provideSearchFilmsViewModelFactory(root.context))
            .get(SearchFilmsViewModel::class.java)

        filmList = root.findViewById(R.id.film_list)
        searchInput = root.findViewById(R.id.search_films)
        emptyList = root.findViewById(R.id.empty_list)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        filmList.addItemDecoration(decoration)
        setupScrollListener()

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.search(query)
        initSearch(query)

        return root
    }

    private fun initAdapter() {
        filmList.adapter = adapter
        viewModel.films.observe(viewLifecycleOwner, Observer<List<Film>> {
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.errors.observe(viewLifecycleOwner, Observer<String> {
            Log.d("FilmListFragment", "Wooops $it")
        })
    }

    private fun initSearch(query: String) {
        searchInput.setText(query)

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateFilmListFromInput()
                true
            } else {
                false
            }
        }
        searchInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateFilmListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateFilmListFromInput() {
        searchInput.text!!.trim().let {
            filmList.scrollToPosition(0)
            viewModel.search(it.toString())
            adapter.submitList(null)
        }

    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            filmList.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            filmList.visibility = View.VISIBLE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = filmList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        filmList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                viewModel.listScrolled(visibleItemCount, lastVisibleItem, totalItemCount)
            }
        })
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_query"
        private const val DEFAULT_QUERY: String = ""
    }
}