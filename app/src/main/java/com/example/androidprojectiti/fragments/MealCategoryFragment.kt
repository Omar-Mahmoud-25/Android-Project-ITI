package com.example.androidprojectiti.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.androidprojectiti.network.ApiClient
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryFactory
import com.example.androidprojectiti.viewModels.MealCategory.MealCategoryViewModel
import retrofit2.Retrofit


class MealCategoryFragment : Fragment() {

    private lateinit var retrofit: MealCategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_category, container, false)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val factory = MealCategoryFactory(
            mealRepo = mealRepoImp(remoteDataSource = ApiClient),
            userRepo = UserRepoImp(LocalDataSourceImp(requireContext()))
        )

        retrofit = ViewModelProvider(this, factory).get(MealCategoryViewModel::class.java)


        val email = arguments?.getString("email")
        val mealList = view.findViewById<RecyclerView>(R.id.recycler_view_meal_category)
        retrofit.mealsList.observe(viewLifecycleOwner){
            Log.d("asd", mealList.toString())
            val adapter = MealCategoryAdapter(
                it,
                UserRepoImp(LocalDataSourceImp(requireContext())),
                lifecycleScope = lifecycleScope,
                email = email ?: "guest",
                navController = findNavController()
            )
            mealList.adapter = adapter
            mealList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter.notifyDataSetChanged()
        }

    }

}