package com.example.androidprojectiti.viewModels

import androidx.lifecycle.ViewModel
import com.example.androidprojectiti.database.User

class SignUpViewModel : ViewModel() {
    private val _emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private val _passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}$")
    private fun validateEmail(email: String):Boolean{
        if (email.matches(_emailRegex)/* and not in the data base */)
            return true
        else
            return false
    }

    private fun validatePassword():Boolean{
        return true
    }

    fun addUser(user: User):Boolean{
        return true
    }

    // to be continued
}