package com.example.androidprojectiti.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidprojectiti.viewModels.SignUpViewModel

class SignUpViewModelFactory : ViewModelProvider.Factory {
    override fun <T: ViewModel> create(modelClass: Class<T>):T{
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java))
            SignUpViewModel() as T
        else
            throw IllegalArgumentException("You have to pass SignUpViewModel")
    }
}