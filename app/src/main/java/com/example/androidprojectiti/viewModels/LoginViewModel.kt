package com.example.androidprojectiti.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginViewModel (private val _userRepo:UserRepo) : ViewModel() {
    suspend fun validateUser(email: TextInputLayout,password: TextInputLayout):Boolean{
        val userList = _userRepo.getUser(email.editText?.text.toString())
        if (userList.isEmpty()) {
            email.error = "This email has no account"
            return false
        }
        else if (userList[0].password != password.editText?.text.toString()) {
            password.error = "Password is incorrect"
            return false
        }
        return true
    }
}