package com.example.androidprojectiti.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.androidprojectiti.R
import com.example.androidprojectiti.RecipeActivity

class SplashFragment : Fragment() {

    private lateinit var lottiee: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lottiee = view.findViewById(R.id.lottie)
        lottiee.repeatCount = 0
        lottiee.playAnimation()

        lottiee.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)

                // Logic to execute after animation ends
                val sharedPreferences = requireActivity().getSharedPreferences("logging_details", Context.MODE_PRIVATE)
                if (sharedPreferences.getBoolean("isUserLoggedIn", false)) {
                    val intent = Intent(requireContext(), RecipeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish() // Finish the splash activity to prevent going back to it
                } else {
                    findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                }
            }
        })
    }
}
