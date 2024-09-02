package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
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
import androidx.cardview.widget.CardView
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
    private lateinit var list_of_Categories: List<Category>
    private lateinit var list_of_meal: List<Meal>
    private lateinit var randomMeal: Meal
    private lateinit var retrofitViewModel: HomeViewModel
    private lateinit var network: NetworkLiveData
    private lateinit var materialCardView: CardView
    private lateinit var lottieAnimationView: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = "Home"

        // Initialize views
        materialCardView = view.findViewById(R.id.materialCardView)
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)
        lottieAnimationView.visibility = View.VISIBLE

        network = NetworkLiveData(requireContext())

        val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", "guest")

        val factoryClass = FactoryClassHome(
            categoryRepositry = categoryRepoImp(remoteDataSource = ApiClient),
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        )
        retrofitViewModel = ViewModelProvider(this, factoryClass).get(HomeViewModel::class.java)

        network.observe(requireActivity()) {
            if (it) {
                retrofitViewModel.getRandomMeal()
                retrofitViewModel.getMeals()
                retrofitViewModel.getCategories()
            } else {
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
            }
        }

        val categoryList = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        val categoryAdapter = CategoryAdapter(
            emptyList(),
            email = email ?: "guest",
            navController = findNavController())
        //list_of_Categories = it
        categoryList.adapter = categoryAdapter
        categoryList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        retrofitViewModel.CategoryList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                categoryAdapter.setListOfMeal(it)
            }
        }


        val Mealslist = view.findViewById<RecyclerView>(R.id.meal_recycler_view)
        val mealAdapter = MealAdapter(
            emptyList(),
            UserRepoImp(LocalDataSourceImp(requireContext())),
            lifecycleScope = lifecycleScope,
            email = email ?: "guest",
            navController = findNavController()
        )
        Mealslist.adapter = mealAdapter
        Mealslist.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        retrofitViewModel.MealsList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()){
                mealAdapter.setListOfMeal(it)
            }

            // Hide animation once data is loaded
            lottieAnimationView.visibility = View.GONE
        }




        val name = view.findViewById<TextView>(R.id.Name)
        val nameOfUser = view.findViewById<TextView>(R.id.nameOfUser)
        val image = view.findViewById<ImageView>(R.id.imageView)
        val favouriteButton = view.findViewById<ImageButton>(R.id.heart_button)

        retrofitViewModel.getUserName(email ?: "guest")
        retrofitViewModel.userName.observe(viewLifecycleOwner) {
            nameOfUser.text = it
        }


        retrofitViewModel.RandomMeal.observe(viewLifecycleOwner) { meals ->
            if (meals.isNotEmpty()) {
                randomMeal = meals[0]
                name.text = "This ${randomMeal.strMeal} will warm up the faintest of hearts."

                Glide.with(image.context)
                    .load(randomMeal.strMealThumb)
                    .placeholder(R.drawable.baseline_arrow_circle_down_24)
                    .error(R.drawable.baseline_error_24)
                    .into(image)

                materialCardView.setOnClickListener {
                    randomMeal.putDefaults()
                    val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(randomMeal)
                    findNavController().navigate(action)
                }

                lifecycleScope.launch {
                    val userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
                    val favoriteMeals = userRepo.getUserFavoriteMeals(email ?: "guest")
                    var isFavorite = favoriteMeals.contains(randomMeal)

                    favouriteButton.setImageResource(
                        if (isFavorite) R.drawable.red_heart else R.drawable.white_heart
                    )
                    favouriteButton.setOnClickListener {
                        if (isFavorite) {
                            favouriteButton.setImageResource(R.drawable.white_heart)
                            lifecycleScope.launch {
                                userRepo.deleteMealFromFav(UserFavorites(email ?: "guest", randomMeal))
                                Toast.makeText(requireContext(), "${randomMeal.strMeal} removed from favorites", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            favouriteButton.setImageResource(R.drawable.red_heart)
                            lifecycleScope.launch {
                                userRepo.insertMealToFav(UserFavorites(email ?: "guest", randomMeal))
                                Toast.makeText(requireContext(), "${randomMeal.strMeal} added to favorites", Toast.LENGTH_SHORT).show()
                            }
                        }
                        isFavorite=!isFavorite
                    }
                }
            }
        }
    }
}
