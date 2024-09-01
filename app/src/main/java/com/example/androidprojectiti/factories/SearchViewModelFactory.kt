package com.example.androidprojectiti.factories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.viewModels.SearchViewModel
import com.example.androidprojectiti.viewModels.SignUpViewModel

class SearchViewModelFactory(private val _repo: mealRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            SearchViewModel(_repo) as T
        else
            throw IllegalArgumentException("You have to pass SearchViewModel")
    }
}