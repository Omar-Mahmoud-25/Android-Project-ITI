package com.example.androidprojectiti.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidprojectiti.Adapters.CategoryAdapter
import com.example.androidprojectiti.Adapters.MealAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.category.categoryRepoImp
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.dto.CategoryResponse.Category
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.viewModels.Home.FactoryClassHome
import com.example.androidprojectiti.viewModels.Home.HomeViewModel

class HomeFragment : Fragment() {
    lateinit var list_of_Categories: List<Category>
    lateinit var list_of_meal:List<Meal>
    lateinit var randomMeal:Meal
    private lateinit var retrofitViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factoryClass = FactoryClassHome(
            categoryRepositry = categoryRepoImp(
                remoteDataSource = ApiClient
            ),
            mealRepo=mealRepoImp(
                remoteDataSource = ApiClient
            )
        )
        retrofitViewModel = ViewModelProvider(this, factoryClass)
            .get(HomeViewModel::class.java)
        retrofitViewModel.getCategories()
        retrofitViewModel.getMeals()
        val Categorieslist = view.findViewById<RecyclerView>(R.id.category_recycler_view)
        retrofitViewModel.CategoryList.observe(viewLifecycleOwner) {
            val adapter = CategoryAdapter(it)
            list_of_Categories = it
            Categorieslist.adapter = adapter
            Categorieslist.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
        val Mealslist = view.findViewById<RecyclerView>(R.id.meal_recycler_view)
        retrofitViewModel.MealsList.observe(viewLifecycleOwner) {
            val adapter = MealAdapter(it)
            list_of_meal = it
            Mealslist.adapter = adapter
            Mealslist.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
        var name=view.findViewById<TextView>(R.id.Name)
        var category=view.findViewById<TextView>(R.id.CategoryName)
        var image=view.findViewById<ImageView>(R.id.imageView)
        retrofitViewModel.getRandomMeal()
        retrofitViewModel.RandomMeal.observe(viewLifecycleOwner){
            randomMeal=it[0]
            name.text=randomMeal.strMeal
            category.text=randomMeal.strCategory
            Glide.with(image.context)
                .load(randomMeal.strMealThumb)
                .placeholder(R.drawable.baseline_arrow_circle_down_24)
                .error(R.drawable.baseline_error_24)
                .into(image)
        }


    }

}