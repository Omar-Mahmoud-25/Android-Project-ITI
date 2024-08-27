package com.example.myapplicationrecyclarview

//import ItemViewModel
import SearchViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.R

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var mealSearchAdapter: MealSearchAdapter
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    // THIS FRAGMENT AND SEARCH HAVE NOT BEEN DONE YET

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        noResultsTextView = view.findViewById(R.id.no_results_text_view)
        titleTextView = view.findViewById(R.id.title_text_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        mealSearchAdapter = MealSearchAdapter(emptyList())
        recyclerView.adapter = mealSearchAdapter

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)

        // Observe the items LiveData
        searchViewModel.items.observe(viewLifecycleOwner, Observer { items ->
            mealSearchAdapter.updateData(items)
        })

//         Observe the noMatches LiveData
        searchViewModel.noMatches.observe(viewLifecycleOwner, Observer { noMatches ->
            recyclerView.isVisible = !noMatches
            noResultsTextView.isVisible = noMatches
        })

        // Observe the searchPerformed LiveData
        searchViewModel.searchPerformed.observe(viewLifecycleOwner, Observer { searchPerformed ->
            if (searchPerformed) {
                recyclerView.isVisible = mealSearchAdapter.itemCount > 0
                noResultsTextView.isVisible = mealSearchAdapter.itemCount == 0
            } else {
                recyclerView.isVisible = false
                noResultsTextView.isVisible = false
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchViewModel.searchMeals(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    searchViewModel.searchMeals(it)
                    // Check if newText is empty to hide results
                    if (it.isEmpty()) {
                        searchViewModel.clearSearch()
                    }
                }
                return true
            }
        })

        // Initially hide RecyclerView and no results TextView
        recyclerView.isVisible = false
        noResultsTextView.isVisible = false
    }
}
