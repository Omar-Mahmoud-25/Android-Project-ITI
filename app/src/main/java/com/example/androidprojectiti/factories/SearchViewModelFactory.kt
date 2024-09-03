package com.example.androidprojectiti.factories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.Area.AreaRepo
import com.example.androidprojectiti.Repositry.meal.mealRepo
import com.example.androidprojectiti.viewModels.search.SearchViewModel

class SearchViewModelFactory(private val _repo: mealRepo,private val areaRepo: AreaRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            SearchViewModel(_repo,areaRepo) as T
        else
            throw IllegalArgumentException("You have to pass SearchViewModel")
    }
}