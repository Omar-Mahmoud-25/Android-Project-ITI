package com.example.androidprojectiti

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.example.androidprojectiti.repositories.user.UserRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun onClickFavorite(
    isFavorite:Boolean,
    heart:ImageButton,
    repo:UserRepo,
    lifecycleScope : CoroutineScope,
    context: Context,
    email: String?,
    meal: Meal
){
    if (isFavorite) {
        heart.setImageResource(R.drawable.white_heart)
        lifecycleScope.launch {
            repo.deleteMealFromFav(UserFavorites(email ?: "guest", meal))
            Toast.makeText(
                context,
                "${meal.strMeal} removed from favorites",
                Toast.LENGTH_SHORT
            ).show()

        }

    }
    else {
        heart.setImageResource(R.drawable.red_heart)
        lifecycleScope.launch {
            repo.insertMealToFav(UserFavorites(email ?: "guest", meal))
            Toast.makeText(
                context,
                "${meal.strMeal} added to favorites",
                Toast.LENGTH_SHORT
            ).show()

        }

    }
}