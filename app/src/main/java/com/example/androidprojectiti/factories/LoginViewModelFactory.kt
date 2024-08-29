package com.example.androidprojectiti.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.viewModels.LoginViewModel

class LoginViewModelFactory(private val _repo:UserRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            LoginViewModel(_repo) as T
        else
            throw IllegalArgumentException("You have to pass LoginViewModel")
    }
}