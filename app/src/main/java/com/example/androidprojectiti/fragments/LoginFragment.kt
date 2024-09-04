package com.example.androidprojectiti.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.androidprojectiti.R
import com.example.androidprojectiti.RecipeActivity
import com.example.androidprojectiti.Repositry.user.UserRepo
import com.example.androidprojectiti.Repositry.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.factories.LoginViewModelFactory
import com.example.androidprojectiti.factories.SignUpViewModelFactory
import com.example.androidprojectiti.viewModels.LoginViewModel
import com.example.androidprojectiti.viewModels.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var _viewModel:LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = LoginViewModelFactory(UserRepoImp(LocalDataSourceImp(requireContext())))
        _viewModel = ViewModelProvider(this, factory).get(LoginViewModel::class.java)

        val login = view.findViewById<Button>(R.id.loginButton)
        val dontHaveAccount = view.findViewById<TextView>(R.id.dontHaveAccount)
        val email = view.findViewById<TextInputLayout>(R.id.EmailInputLayout)
        val password = view.findViewById<TextInputLayout>(R.id.PasswordInputLayout2)
        val sharedPreferences = requireActivity().
        getSharedPreferences("logging_details",
            Context.MODE_PRIVATE)

        login.setOnClickListener {
            lifecycleScope.launch {
                if (_viewModel.validateUser(email, password)) {
                    with(sharedPreferences.edit()) {
                        putBoolean("isUserLoggedIn", true)
                        putString("email", email.editText?.text.toString())
                        apply()
                    }
                    val intent = Intent(requireContext(), RecipeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        dontHaveAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }


}