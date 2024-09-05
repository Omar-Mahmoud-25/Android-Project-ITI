package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.MealCategoryAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.category.categoryRepoImp
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
    private lateinit var categoryName:TextView
    private lateinit var categoryImage:ImageView

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
        val bottomNavBar = requireActivity().findViewById<View>(R.id.bottom_nav)
        bottomNavBar.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        activity?.title = arguments?.getString("category")

        loadingAnimation = view.findViewById(R.id.loading_animation)
        categoryName=view.findViewById(R.id.Category_name)
        categoryName.text=arguments?.getString("category")
        categoryImage=view.findViewById(R.id.CayegoryImage)
        Glide.with(categoryImage.context)
            .load(arguments?.getString("image"))
            .placeholder(R.drawable.baseline_arrow_circle_down_24)
            .error(R.drawable.baseline_error_24)
            .into(categoryImage)
        mealList = view.findViewById(R.id.recycler_view_meal_category)
        mealList.layoutManager = GridLayoutManager(requireContext(),2)

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
    override fun onDestroyView() {
        super.onDestroyView()

        val bottomNavBar = requireActivity().findViewById<View>(R.id.bottom_nav)
        bottomNavBar.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()  // Navigate back to the previous fragment
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
