package com.example.androidprojectiti.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.repositories.user.UserRepo

class FavoriteViewModelFactory(private val _repo: UserRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(FavouriteViewModel::class.java))
            FavouriteViewModel(_repo) as T
        else
            throw IllegalArgumentException("You have to pass LoginViewModel")
    }
}