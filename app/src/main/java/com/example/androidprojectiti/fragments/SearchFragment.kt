package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.factories.SearchViewModelFactory
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.SearchViewModel
import com.example.myapplicationrecyclarview.MealSearchAdapter

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsAnimationView: LottieAnimationView
    private lateinit var mealSearchAdapter: MealSearchAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var network: NetworkLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Search"

        // Initialize views
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        noResultsAnimationView = view.findViewById(R.id.no_results_animation_view)

        // Initialize network live data
        network = NetworkLiveData(requireContext())

        // Initialize RecyclerView and adapter
        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")
        recyclerView.layoutManager = LinearLayoutManager(context)
        mealSearchAdapter = MealSearchAdapter(emptyList(),
            UserRepoImp(LocalDataSourceImp(view.context)),
            email = email ?: "guest",
            lifecycleScope = lifecycleScope,
            findNavController())
        recyclerView.adapter = mealSearchAdapter

        // Initialize ViewModel
        val factory = SearchViewModelFactory(mealRepoImp(ApiClient))
        searchViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        // Set up network and search functionality
        network.observe(viewLifecycleOwner) { connected ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    handleSearchQuery(query, connected)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    handleSearchQuery(newText, connected)
                    return true
                }
            })

            // Observe search results and update UI accordingly
            searchViewModel.items.observe(viewLifecycleOwner, Observer { items ->
                mealSearchAdapter.updateData(items)
                updateUIBasedOnSearchResults(items)
            })

            // Observe search performed status
            searchViewModel.searchPerformed.observe(viewLifecycleOwner, Observer { searchPerformed ->
                if (!searchPerformed) {
                    recyclerView.isVisible = false
                    noResultsAnimationView.isVisible = false
                }
            })
        }

        // Initially hide RecyclerView and no results animation
        recyclerView.isVisible = false
        noResultsAnimationView.isVisible = false
    }

    private fun handleSearchQuery(query: String?, connected: Boolean) {
        if (connected) {
            searchViewModel.searchMeals(query ?: "")
        } else {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUIBasedOnSearchResults(items: List<Meal>) {
        val query = searchView.query.toString().trim()
        when {
            query.isEmpty() -> {
                // Query is empty
                if (items.isEmpty()) {
                    // No results and no query
                    recyclerView.isVisible = false
                    noResultsAnimationView.isVisible = false
                } else {
                    // Data is available but no query
                    recyclerView.isVisible = false
                    noResultsAnimationView.isVisible = false
                }
            }
            items.isEmpty() -> {
                // Query is not empty but no results
                recyclerView.isVisible = false
                noResultsAnimationView.isVisible = true
            }
            else -> {
                // Query is not empty and results are found
                recyclerView.isVisible = true
                noResultsAnimationView.isVisible = false
            }
        }
    }
}
