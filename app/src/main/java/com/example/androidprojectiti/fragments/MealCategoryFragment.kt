package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.Adapters.MealCategoryAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryFactory
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryViewModel

class MealCategoryFragment : Fragment() {

    private lateinit var retrofit: MealCategoryViewModel
    private lateinit var network: NetworkLiveData
    private lateinit var loadingAnimation: LottieAnimationView
    private lateinit var mealList: RecyclerView
    private lateinit var adapter: MealCategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_category, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingAnimation = view.findViewById(R.id.loading_animation)
        mealList = view.findViewById(R.id.recycler_view_meal_category)
        mealList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // Initialize adapter with empty list
        adapter = MealCategoryAdapter(
            emptyList(),
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext())),
            lifecycleScope = lifecycleScope,
            email = arguments?.getString("email") ?: "guest",
            cat = arguments?.getString("category") ?: "",
            navController = findNavController()
        )
        mealList.adapter = adapter

        network = NetworkLiveData(requireContext())
        val factory = MealCategoryFactory(
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        )
        retrofit = ViewModelProvider(this, factory).get(MealCategoryViewModel::class.java)

        network.observe(requireActivity()) {
            it?.let{
                if (it)
                    retrofit.getMealsByCategory(arguments?.getString("category").toString())
                else
                    Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
            }

        }

        retrofit.mealsList.observe(viewLifecycleOwner) { meals ->
            meals?.let{
                if (meals.isEmpty()) {
                    // Show Lottie animation if no data
                    loadingAnimation.visibility = View.VISIBLE
                    mealList.visibility = View.GONE
                }
                else {
                    // Show RecyclerView and hide Lottie animation
                    loadingAnimation.visibility = View.GONE
                    mealList.visibility = View.VISIBLE
                    adapter.setListOfMeal(meals)
                }
            }
        }
    }
}
