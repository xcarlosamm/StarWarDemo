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
import co.swapi.demo.model.People
import co.swapi.demo.ui.adapter.PeopleAdapter
import co.swapi.demo.ui.viewmodel.SearchPeopleViewModel
import com.google.android.material.textfield.TextInputEditText

class PeopleFragment : Fragment() {

    private lateinit var viewModel: SearchPeopleViewModel
    private val adapter = PeopleAdapter()

    private lateinit var peopleList: RecyclerView
    private lateinit var searchInput: TextInputEditText
    private lateinit var emptyList: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_people, container, false)

        // get the view model
        viewModel = ViewModelProvider(this, InjectionUtils.provideSearchPeopleViewModelFactory(root.context))
            .get(SearchPeopleViewModel::class.java)

        peopleList = root.findViewById(R.id.people_list)
        searchInput = root.findViewById(R.id.search_people)
        emptyList = root.findViewById(R.id.empty_list)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        peopleList.addItemDecoration(decoration)
        setupScrollListener()

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.search(query)
        initSearch(query)

        return root
    }

    private fun initAdapter() {
        peopleList.adapter = adapter
        viewModel.people.observe(viewLifecycleOwner, Observer<List<People>> {
            showEmptyList(it?.size == 0)
            adapter.submitList(it)
        })
        viewModel.errors.observe(viewLifecycleOwner, Observer<String> {
            Log.d("PeopleListFragment", "Wooops $it")
        })
    }

    private fun initSearch(query: String) {
        searchInput.setText(query)

        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updatePeopleFromInput()
                true
            } else {
                false
            }
        }
        searchInput.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updatePeopleFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updatePeopleFromInput() {
        searchInput.text!!.trim().let {
            peopleList.scrollToPosition(0)
            viewModel.search(it.toString())
            adapter.submitList(null)
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            peopleList.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            peopleList.visibility = View.VISIBLE
        }
    }

    private fun setupScrollListener() {
        val layoutManager = peopleList.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
        peopleList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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