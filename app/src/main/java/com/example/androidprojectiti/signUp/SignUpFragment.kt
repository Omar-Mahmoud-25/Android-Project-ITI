package com.example.androidprojectiti.signUp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.androidprojectiti.R
import com.example.androidprojectiti.repositories.user.UserRepoImp
import com.example.androidprojectiti.database.LocalDataSourceImp
import com.example.androidprojectiti.factories.SignUpViewModelFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class SignUpFragment : Fragment() {

    private lateinit var _viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = SignUpViewModelFactory(UserRepoImp(LocalDataSourceImp(requireContext())))
        _viewModel = ViewModelProvider(this, factory).get(SignUpViewModel::class.java)

        val firstName = view.findViewById<TextInputLayout>(R.id.firstNameTextInput)
        val email = view.findViewById<TextInputLayout>(R.id.emailTextInput)
        val age = view.findViewById<TextInputLayout>(R.id.ageTextInput)
        val password = view.findViewById<TextInputLayout>(R.id.passwordTextInput)
        val confirmPassword = view.findViewById<TextInputLayout>(R.id.confirmPasswordTextInput)
        val signUp = view.findViewById<Button>(R.id.signUpButton)
        val haveAccount = view.findViewById<TextView>(R.id.haveAccountText)
//        lottieAnimationView = view.findViewById(R.id.lottieAnimationView)

        haveAccount.setOnClickListener{
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        signUp.setOnClickListener {
            lifecycleScope.launch {
                if (_viewModel.makeUser(
                        firstName = firstName,
                        age = age,
                        password = password,
                        email = email,
                        confirmPassword = confirmPassword
                    )
                ) {
                    findNavController().navigate(R.id.action_signUpFragment_to_dialogFragment)
                }
            }
        }
    }
}
