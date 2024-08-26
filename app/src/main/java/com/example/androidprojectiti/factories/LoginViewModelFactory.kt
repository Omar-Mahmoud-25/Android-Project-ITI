package com.example.androidprojectiti.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.viewModels.LoginViewModel

class LoginViewModelFactory : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            LoginViewModel() as T
        else
            throw IllegalArgumentException("You have to pass LoginViewModel")
    }
}