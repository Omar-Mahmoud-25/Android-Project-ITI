package com.example.androidprojectiti
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI

class RecipeActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food)

        bottomNavigationView = findViewById(R.id.bottom_nav)
        toolbar = findViewById(R.id.toolbar)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Flavor Fusion"

        sharedPreferences = this.
        getSharedPreferences("logging_details",
            Context.MODE_PRIVATE)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home_btm -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.search_btm -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.favorite_btm -> {
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.about -> {
                navController.navigate(R.id.aboutFragment)
                true
            }
            R.id.logOut -> {
                with(sharedPreferences.edit()) {
                    putBoolean("isUserLoggedIn", false)
                    putString("email", "")
                    apply()
                }
                val intent = Intent(this, SplashActivity::class.java)
                intent.putExtra("isUserLoggingOut",true)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                true
            }
            else -> false
        }
    }

    // Assuming you have this method defined somewhere in your activity
    private fun enableEdgeToEdge() {
        // Implementation here
    }
}
