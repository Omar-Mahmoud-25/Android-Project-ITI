package com.example.androidprojectiti.viewModels.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.category.categoryRepo
import com.example.androidprojectiti.Repositry.meal.mealRepo

class FactoryClassHome (

    private val categoryRepositry: categoryRepo,
    private val mealRepo: mealRepo
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(categoryRepositry,mealRepo) as T
        }else{
            throw IllegalArgumentException("Home View Model class not found")
        }
    }
}