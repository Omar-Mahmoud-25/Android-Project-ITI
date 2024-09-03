package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
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

import com.example.myapplicationrecyclarview.MealSearchAdapter
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.Repositry.Area.AreaRepoImp
import com.example.androidprojectiti.viewModels.search.SearchViewModel

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
//    private lateinit var noResultsAnimationView: LottieAnimationView
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
        searchView = view.findViewById(R.id.search_view)
        recyclerView = view.findViewById(R.id.recycler_view)
        network = NetworkLiveData(requireContext())
//        noResultsAnimationView = view.findViewById(R.id.lottie)
        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")
        recyclerView.layoutManager = LinearLayoutManager(context)
        mealSearchAdapter = MealSearchAdapter(
            emptyList(),
            UserRepoImp(LocalDataSourceImp(view.context)),
            email = email ?: "guest",
            lifecycleScope = lifecycleScope,
            findNavController()
        )
        recyclerView.adapter = mealSearchAdapter

        val factory = SearchViewModelFactory(mealRepoImp(ApiClient),AreaRepoImp(ApiClient))
        searchViewModel = ViewModelProvider(this, factory).get(SearchViewModel::class.java)

        network.observe(viewLifecycleOwner) { connected ->
            connected?.let{
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (connected)
                            query?.let { searchViewModel.searchMeals(it) }
                        else
                            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG)
                                .show()
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (connected)
                            searchViewModel.searchMeals(newText ?: "")
                        else
                            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG)
                                .show()
                        return true
                    }
                })

                // Observe the items LiveData
                searchViewModel.items.observe(viewLifecycleOwner, Observer { items ->
                    items?.let{
                        mealSearchAdapter.updateData(items)

                        // Update visibility of RecyclerView and no results animation based on items count and query
                        val query = searchView.query.toString()
                        if (items.isEmpty() && query.isNotEmpty()) {
                            recyclerView.isVisible = false
//                            noResultsAnimationView.isVisible = true
                        } else {
                            recyclerView.isVisible = items.isNotEmpty()
//                            noResultsAnimationView.isVisible = items.isEmpty() && query.isNotEmpty()
                        }
                    }
                })

                // Handle visibility when search is performed or not
                searchViewModel.searchPerformed.observe(viewLifecycleOwner) { searchPerformed ->
                    searchPerformed?.let{
                        if (!searchPerformed) {
                            recyclerView.isVisible = false
//                            noResultsAnimationView.isVisible = false
                        }
                    }
                }
            }
        }

        // Initially hide RecyclerView and no results animation
        recyclerView.isVisible = false
//        noResultsAnimationView.isVisible = false
    }
}
