package com.example.androidprojectiti.viewModels.Home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.category.categoryRepo

class FactoryClassHome (
    private val categoryRepositry: categoryRepo
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(categoryRepositry) as T
        }else{
            throw  IllegalArgumentException("Home View Model class not found")
        }
    }
}