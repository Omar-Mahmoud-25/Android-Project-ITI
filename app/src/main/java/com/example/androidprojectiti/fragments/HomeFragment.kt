package com.example.androidprojectiti.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.CategoryAdapter
import com.example.androidprojectiti.Adapters.MealAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.category.categoryRepoImp
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.viewModels.Home.FactoryClassHome
import com.example.androidprojectiti.viewModels.Home.HomeViewModel
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var list_of_Categories: List<Category>
    lateinit var list_of_meal: List<Meal>
    lateinit var randomMeal: Meal
    private lateinit var retrofitViewModel: HomeViewModel
    private lateinit var network: NetworkLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        network = NetworkLiveData(requireContext())

        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")

        val factoryClass = FactoryClassHome(
            categoryRepositry = categoryRepoImp(remoteDataSource = ApiClient),
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        )

        retrofitViewModel = ViewModelProvider(this, factoryClass)
            .get(HomeViewModel::class.java)

        // Find LottieAnimationView
        val lottieAnimationView: LottieAnimationView = view.findViewById(R.id.lottieAnimationView)

        // Show animation when data is loading
        fun showLoading() {
            lottieAnimationView.visibility = View.VISIBLE
            lottieAnimationView.playAnimation()
        }

        // Hide animation when data is loaded or error occurs
        fun hideLoading() {
            lottieAnimationView.visibility = View.GONE
            lottieAnimationView.cancelAnimation()
        }

        network.observe(requireActivity()) {
            if (it) {
                showLoading()
                retrofitViewModel.getMeals()
                retrofitViewModel.getCategories()
            } else {
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
            }
        }

        val Categorieslist = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        retrofitViewModel.CategoryList.observe(viewLifecycleOwner) {
            hideLoading()
            val adapter = CategoryAdapter(it)
            list_of_Categories = it
            Categorieslist.adapter = adapter
            Categorieslist.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val Mealslist = view.findViewById<RecyclerView>(R.id.meal_recycler_view)
        retrofitViewModel.MealsList.observe(viewLifecycleOwner) {
            hideLoading()
            val adapter = MealAdapter(
                it,
                UserRepoImp(LocalDataSourceImp(requireContext())),
                lifecycleScope = lifecycleScope,
                email = email ?: "guest"
            )
            list_of_meal = it
            Mealslist.adapter = adapter
            Mealslist.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val name = view.findViewById<TextView>(R.id.Name)
        val nameOfUser = view.findViewById<TextView>(R.id.nameOfUser)
        val category = view.findViewById<TextView>(R.id.CategoryName)
        val image = view.findViewById<ImageView>(R.id.imageView)
        val favouriteButton = view.findViewById<ImageButton>(R.id.heart_button)

        retrofitViewModel.getUserName(email ?: "guest")
        retrofitViewModel.userName.observe(viewLifecycleOwner) {
            nameOfUser.text = it
        }

        retrofitViewModel.getRandomMeal()
        retrofitViewModel.RandomMeal.observe(viewLifecycleOwner) {
            randomMeal = it[0]
            name.text = randomMeal.strMeal
            category.text = randomMeal.strCategory
            Glide.with(image.context)
                .load(randomMeal.strMealThumb)
                .placeholder(R.drawable.baseline_arrow_circle_down_24)
                .error(R.drawable.baseline_error_24)
                .into(image)

            lifecycleScope.launch {
                val userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
                val favoriteMeals = userRepo.getUserFavoriteMeals(email ?: "guest")
                var isFavorite = favoriteMeals.contains(randomMeal.idMeal)

                favouriteButton.setImageResource(
                    if (isFavorite) {
                        R.drawable.red_heart
                    } else {
                        R.drawable.white_heart
                    }
                )

                favouriteButton.setOnClickListener {
                    if (isFavorite) {
                        favouriteButton.setImageResource(R.drawable.white_heart)
                        lifecycleScope.launch {
                            userRepo.deleteMealFromFav(UserFavorites(email ?: "guest", randomMeal.idMeal))
                            Toast.makeText(
                                requireContext(),
                                "${randomMeal.strMeal} removed from favorites", Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        favouriteButton.setImageResource(R.drawable.red_heart)
                        lifecycleScope.launch {
                            userRepo.insertMealToFav(UserFavorites(email ?: "guest", randomMeal.idMeal))
                            Toast.makeText(
                                requireContext(),
                                "${randomMeal.strMeal} added to favorites", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    isFavorite = !isFavorite
                }
            }
        }
    }
}
