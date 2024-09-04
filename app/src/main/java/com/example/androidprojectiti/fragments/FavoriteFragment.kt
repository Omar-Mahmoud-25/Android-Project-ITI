package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.Adapters.favoriteAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.viewModels.Favorite.FavouriteViewModel
import com.example.androidprojectiti.swipeToDeleteFromFav

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

        favViewModel = FavouriteViewModel(userRepo)
        favViewModel.getFavMeals(email)

        loadingAnimation = view.findViewById(R.id.loading_animation2)
        noFavoritesAnimation = view.findViewById(R.id.loading_animation_no)
        val favMealsList = view.findViewById<RecyclerView>(R.id.fav_recycler_view)
        favMealsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initially show the loading animation and hide both animations and RecyclerView
        loadingAnimation.visibility = View.VISIBLE
        noFavoritesAnimation.visibility = View.GONE
        favMealsList.visibility = View.GONE

        favViewModel.mealsList.observe(viewLifecycleOwner) { meals ->
            meals?.let{
                if (meals.isEmpty()) {
                    // If no data, show no favorites animation
                    loadingAnimation.visibility = View.GONE
                    noFavoritesAnimation.visibility = View.VISIBLE
                    favMealsList.visibility = View.GONE
                } else {
                    // Hide animations and show RecyclerView
                    loadingAnimation.visibility = View.GONE
                    noFavoritesAnimation.visibility = View.GONE
                    favMealsList.visibility = View.VISIBLE
                    adapter = favoriteAdapter(
                        meals.toMutableList(),
                        userRepo,
                        email,
                        lifecycleScope,
                        requireContext(),
                        { onConfirm ->
                            showConfirmationDialog(onConfirm)
                        },
                        findNavController()
                    )
                    favMealsList.adapter = adapter

                    val itemTouchHelper = ItemTouchHelper(
                        swipeToDeleteFromFav(
                            adapter,
                            requireContext(),
                            ::showConfirmationDialog
                        )
                    )
                    itemTouchHelper.attachToRecyclerView(favMealsList)
                }
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
