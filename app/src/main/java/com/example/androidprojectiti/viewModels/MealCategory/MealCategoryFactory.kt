package com.example.androidprojectiti.viewModels.MealCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo

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