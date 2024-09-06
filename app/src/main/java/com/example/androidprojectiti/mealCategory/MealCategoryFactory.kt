package com.example.androidprojectiti.mealCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.repositories.meal.mealRepo
import com.example.androidprojectiti.repositories.user.UserRepo

class MealCategoryFactory(
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MealCategoryViewModel :: class.java)){
            MealCategoryViewModel(mealRepo, userRepo) as T
        }else
            throw IllegalArgumentException("ProductViewModel class not found")

    }

}