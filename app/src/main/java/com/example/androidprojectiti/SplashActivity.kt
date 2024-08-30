package com.example.androidprojectiti

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        if (intent.getBooleanExtra("isUserLoggingOut",false))
            (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment)
                .navController
                .navigate(R.id.action_splashFragment_to_loginFragment)

    }
}
