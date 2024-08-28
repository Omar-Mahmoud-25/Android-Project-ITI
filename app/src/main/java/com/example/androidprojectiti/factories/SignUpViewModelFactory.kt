package com.example.androidprojectiti.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.viewModels.SignUpViewModel

class SignUpViewModelFactory(private val _repo: UserRepo) : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java))
            SignUpViewModel(_repo) as T
        else
            throw IllegalArgumentException("You have to pass SignUpViewModel")
    }
}