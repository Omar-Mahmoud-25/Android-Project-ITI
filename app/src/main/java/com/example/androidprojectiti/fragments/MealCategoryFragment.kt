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
import com.example.androidprojectiti.Adapters.MealCategoryAdapter
import com.example.androidprojectiti.R
import com.example.androidprojectiti.Repositry.meal.mealRepoImp
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.network.NetworkLiveData
import com.example.androidprojectiti.network.RemoteDataSource
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryFactory
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryViewModel
import retrofit2.Retrofit


class MealCategoryFragment : Fragment() {

    private lateinit var retrofit: MealCategoryViewModel
    private lateinit var network: NetworkLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_category, container, false)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        network = NetworkLiveData(requireContext())


        val factory = MealCategoryFactory(
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        )

        retrofit = ViewModelProvider(this, factory).get(MealCategoryViewModel::class.java)

        Log.d("asd", "hello")
        val email = arguments?.getString("email")
        val category = arguments?.getString("category")
        Log.d("asd", email.toString())

        network.observe(requireActivity()) {
            if (it) {
                retrofit.getMealsByCategory(category.toString())
            } else {
                Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_LONG).show()
            }
        }

        val mealList = view.findViewById<RecyclerView>(R.id.recycler_view_meal_category)
        val adapter = MealCategoryAdapter(
            emptyList(),
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            UserRepoImp(LocalDataSourceImp(requireContext())),
            lifecycleScope = lifecycleScope,
            email = email ?: "guest",
            cat = category.toString(),
            navController = findNavController()
        )
        mealList.adapter = adapter
        mealList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        var meals = mutableListOf<Meal>()
        retrofit.mealsList.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                adapter.setListOfMeal(it)
            }
        }

    }

}