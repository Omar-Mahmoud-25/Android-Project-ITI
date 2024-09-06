package com.example.androidprojectiti.login

import androidx.lifecycle.ViewModel
import com.example.androidprojectiti.repositories.user.UserRepo
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel (private val _userRepo:UserRepo) : ViewModel() {
    suspend fun validateUser(email: TextInputLayout,password: TextInputLayout):Boolean{
        val userList = _userRepo.getUser(email.editText?.text.toString())
        return when{
            userList.isEmpty() -> {
                email.error = "This email has no account"
                false
            }
            userList[0].password != password.editText?.text.toString() -> {
                password.error = "Password is incorrect"
                false
            }
            else -> true
        }
    }
}