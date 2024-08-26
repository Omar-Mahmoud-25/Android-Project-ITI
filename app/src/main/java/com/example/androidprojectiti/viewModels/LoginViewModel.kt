package com.example.androidprojectiti.viewModels

import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginViewModel : ViewModel() {
    fun validateUser(email: TextInputLayout,password: TextInputLayout):Boolean{
        /*
        * get the user with this email
        * if (there is a user)
        *   if (user.password == password)
        *       return true
        *   else {
        *       password.error = "wrong password"
        *       return false
        *   }
        * else{
        *   email.error = "this email doesn't have an account"
        *   return false
        * }
        * */
        return true
    }
}