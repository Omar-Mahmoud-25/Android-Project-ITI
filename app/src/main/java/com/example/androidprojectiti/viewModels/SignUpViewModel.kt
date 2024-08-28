package com.example.androidprojectiti.viewModels

import androidx.lifecycle.ViewModel
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.database.entity.User
import com.google.android.material.textfield.TextInputLayout
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignUpViewModel (private val _userRepo : UserRepo) : ViewModel() {
    private val _emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    private val _passwordRegex = Regex("^(?=.*[0-9])(?=.*[a-zA-Z]).{8,16}$")
    private fun validateEmail(email: String):Boolean{
        return email.matches(_emailRegex)
    }

    private fun validatePassword(password: String):Boolean{
        return password.matches(_passwordRegex)
    }

    private fun validateConfirmPassword(confirm:String,password: String):Boolean{
        return password == confirm
    }

    suspend fun makeUser(
        firstName: TextInputLayout,
        lastName: TextInputLayout,
        age:TextInputLayout,
        email:TextInputLayout,
        password:TextInputLayout,
        confirmPassword : TextInputLayout
    ):Boolean{
        val firstNameText = firstName.editText?.text.toString()
        val emailText = email.editText?.text.toString()
        val passwordText = password.editText?.text.toString()
        val confirmPasswordText = confirmPassword.editText?.text.toString()
        val ageText = age.editText?.text.toString()
        val lastNameText = lastName.editText?.text.toString()
        var valid = true
        if (firstNameText.isEmpty()) {
            firstName.error = "This field is required"
            return false
        }
        else
            firstName.error = ""
        if (lastNameText.isEmpty()){
            lastName.error = "This field is required"
            return false
        }
        else
            lastName.error = ""
        if (ageText.isEmpty()){
            age.error = "This field is required"
            return false
        }
        else
            age.error = ""
        if (!validateEmail(emailText)) {
            email.error = "Please enter a valid Email!"
            return false
        }
        else
            email.error = ""
        if (!validatePassword(passwordText)) {
            password.error =
                "Password must be between 8 and 16 character, containing numbers, upper and lowercase letters"
            return false
        }
        else
            password.error = ""
        if (!validateConfirmPassword(confirmPasswordText,passwordText)) {
            confirmPassword.error = "Password doesn't match"
            return false
        }
        else
            confirmPassword.error = ""

        // if this email is in the database, return false

        val user = User(
            firstName = firstNameText,
            lastName = lastNameText,
            age = ageText.toInt(),
            email = emailText,
            password = passwordText
        )
        if (_userRepo.getUser(email.editText?.text.toString()).isEmpty())
            _userRepo.insertUser(user)
        else{
            email.error = "This email already have an account"
            return false
        }
        return true
    }

    // to be continued
}