package com.example.androidprojectiti.favorite

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.R
import com.example.androidprojectiti.repositories.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.repositories.meal.mealRepoImp
import com.example.androidprojectiti.search.SearchViewModel
import com.example.androidprojectiti.search.SearchViewModelFactory

class FavoriteFragment : Fragment() {

    private lateinit var favViewModel: FavouriteViewModel
    private lateinit var adapter: favoriteAdapter
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var noFavoritesAnimation: LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Favorites"

        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest") ?: "guest"

        val userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        val factory = FavoriteViewModelFactory(userRepo)
        favViewModel = ViewModelProvider(this, factory).get(FavouriteViewModel::class.java)
        favViewModel.getFavMeals(email)

        loadingAnimation = view.findViewById(R.id.loading_animation2)
        noFavoritesAnimation = view.findViewById(R.id.loading_animation_no)
        val favMealsList = view.findViewById<RecyclerView>(R.id.fav_recycler_view)
        favMealsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        adapter = favoriteAdapter(
            emptyList<Meal>().toMutableList(),
            userRepo,
            email,
            lifecycleScope,
            requireContext(),
            favViewModel,
            { onConfirm ->
                showConfirmationDialog(onConfirm)
            },
            findNavController()
        )
        favMealsList?.adapter = adapter
        val itemTouchHelper = ItemTouchHelper(
            swipeToDeleteFromFav(
                adapter,
                ::showConfirmationDialog
            )
        )
        itemTouchHelper.attachToRecyclerView(favMealsList)
        loadingAnimation.visibility = View.VISIBLE
        favViewModel.mealsList.observe(viewLifecycleOwner) { meals ->
            if (meals.isNullOrEmpty()) {
                loadingAnimation.visibility = View.GONE
                noFavoritesAnimation.visibility = View.VISIBLE
                favMealsList?.visibility = View.GONE
            } else {
                loadingAnimation.visibility = View.GONE
                noFavoritesAnimation.visibility = View.GONE
                favMealsList?.visibility = View.VISIBLE
                adapter.updateMeals(meals.toMutableList())
            }
        }
    }
    private fun showConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("Remove Favorite")
            .setMessage("Are you sure you want to remove this item from favorites?")
            .setPositiveButton("Yes") { _, _ ->
                onConfirm()
            }
            .setNegativeButton("No", null)
            .show()
    }
}


