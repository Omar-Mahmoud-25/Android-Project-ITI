package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.factories.SearchViewModelFactory
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.SearchViewModel
import com.example.myapplicationrecyclarview.MealSearchAdapter

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var noResultsTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var mealSearchAdapter: MealSearchAdapter
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var network : NetworkLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Search"
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        noResultsTextView = view.findViewById(R.id.no_results_text_view)
        titleTextView = view.findViewById(R.id.title_text_view)
        network = NetworkLiveData(requireContext())
        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")
        recyclerView.layoutManager = LinearLayoutManager(context)
        mealSearchAdapter = MealSearchAdapter(emptyList(),
            UserRepoImp(LocalDataSourceImp(view.context)),
            email = email ?: "guest",
            lifecycleScope = lifecycleScope,
            findNavController())
        recyclerView.adapter = mealSearchAdapter

        val factory = SearchViewModelFactory(mealRepoImp(ApiClient))
        searchViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        network.observe(viewLifecycleOwner){connected->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("uhhh","in submit, ${network.value}")
                    if (connected)
                        query?.let { searchViewModel.searchMeals(it) }
                    else
                        Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d("uhhh","in change, ${network.value}")
                    if (connected)
                        searchViewModel.searchMeals(newText ?: "")
                    else
                        Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
                    return true
                }
            })
            // Observe the items LiveData
            searchViewModel.items.observe(viewLifecycleOwner, Observer { items ->
                mealSearchAdapter.updateData(items)
                Log.d("nadra", "items observer ${items.toString()}")
            })

            // Observe the noMatches LiveData
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
        }


        // Initially hide RecyclerView and no results TextView
        recyclerView.isVisible = false
        noResultsTextView.isVisible = false
    }
}
