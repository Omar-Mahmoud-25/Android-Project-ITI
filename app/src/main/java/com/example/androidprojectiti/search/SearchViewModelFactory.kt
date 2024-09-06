package com.example.androidprojectiti.search


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.repositories.meal.mealRepo

class SearchViewModelFactory(private val _repo: mealRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            SearchViewModel(_repo) as T
        else
            throw IllegalArgumentException("You have to pass SearchViewModel")
    }
}