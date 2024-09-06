package com.example.androidprojectiti.home

import android.content.Context
import android.widget.ImageButton
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.repositories.category.categoryRepo
import com.example.androidprojectiti.repositories.meal.mealRepo
import com.example.androidprojectiti.repositories.user.UserRepo

class FactoryClassHome (
    private val categoryRepository: categoryRepo,
    private val mealRepo: mealRepo,
    private val userRepo: UserRepo,
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(categoryRepository,mealRepo,userRepo) as T
        }else{
            throw IllegalArgumentException("Home View Model class not found")
        }
    }
}