package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private lateinit var loadingAnimation: LottieAnimationView

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
//        val mealRepo = mealRepoImp(ApiClient)

        favViewModel = FavouriteViewModel(userRepo)//, mealRepo)
        favViewModel.getFavMeals(email)

        loadingAnimation = view.findViewById(R.id.loading_animation2)
        val favMealsList = view.findViewById<RecyclerView>(R.id.fav_recycler_view)
        favMealsList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initially show the animation and hide the RecyclerView
        loadingAnimation.visibility = View.VISIBLE
        favMealsList.visibility = View.GONE

        // Hide the animation after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            loadingAnimation.visibility = View.GONE
        }, 2000)

        favViewModel.mealsList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                // If no data, keep showing animation
                loadingAnimation.visibility = View.VISIBLE
                favMealsList.visibility = View.GONE
            } else {
                // Hide animation and show RecyclerView
                loadingAnimation.visibility = View.GONE
                favMealsList.visibility = View.VISIBLE
                adapter = favoriteAdapter(it.toMutableList(), userRepo, email, lifecycleScope,requireContext(),{ onConfirm ->
                    showConfirmationDialog(onConfirm)
                },findNavController())
                favMealsList.adapter = adapter

                val itemTouchHelper = ItemTouchHelper(swipeToDeletFromFav(adapter, requireContext(), ::showConfirmationDialog))
                itemTouchHelper.attachToRecyclerView(favMealsList)
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
