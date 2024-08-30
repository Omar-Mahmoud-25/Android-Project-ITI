package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidprojectiti.Adapters.favoriteAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.swipeToDeletFromFav
import com.example.androidprojectiti.viewModels.Favorite.FavouriteViewModel
import com.example.androidprojectiti.network.ApiClient

class FavoriteFragment : Fragment() {

    private lateinit var favViewModel: FavouriteViewModel
    private lateinit var adapter: favoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest") ?: "guest"


        val userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        val mealRepo = mealRepoImp(ApiClient)


        favViewModel = FavouriteViewModel(userRepo, mealRepo)
        favViewModel.getFavMeals(email)
        val favMealsList = view.findViewById<RecyclerView>(R.id.fav_recycler_view)
        favMealsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favViewModel.MealsList.observe(viewLifecycleOwner) {
            adapter = favoriteAdapter(it.toMutableList(), userRepo, email, lifecycleScope)
            favMealsList.adapter = adapter

            val itemTouchHelper = ItemTouchHelper(swipeToDeletFromFav(adapter, requireContext()))
            itemTouchHelper.attachToRecyclerView(favMealsList)
        }
    }
}
