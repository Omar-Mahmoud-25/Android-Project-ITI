package com.example.androidprojectiti.viewModels

import androidx.lifecycle.ViewModel
import com.example.androidprojectiti.database.entity.User
import com.google.android.material.textfield.TextInputLayout

class SignUpViewModel : ViewModel() {
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

    fun makeUser(
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
            valid = false
        }
        else
            firstName.error = ""
        if (lastNameText.isEmpty()){
            lastName.error = "This field is required"
            valid = false
        }
        else
            lastName.error = ""
        if (ageText.isEmpty()){
            age.error = "This field is required"
            valid = false
        }
        else
            age.error = ""
        if (!validateEmail(emailText)) {
            email.error = "Please enter a valid Email!"
            valid = false
        }
        else
            email.error = ""
        if (!validatePassword(passwordText)) {
            password.error =
                "Password must be between 8 and 16 character, containing numbers, upper and lowercase letters"
            valid = false
        }
        else
            password.error = ""
        if (!validateConfirmPassword(confirmPasswordText,passwordText)) {
            confirmPassword.error = "Password doesn't match"
            valid = false
        }
        else
            confirmPassword.error = ""
        if (!valid)
            return false

        // if this email is in the database, return false

        val user = User(
            firstName = firstNameText,
            lastName = lastNameText,
            age = ageText.toInt(),
            email = emailText,
            password = passwordText
        )

        // insert user to data base

        return true
    }

    // to be continued
}