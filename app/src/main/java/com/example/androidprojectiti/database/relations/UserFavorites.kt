package com.example.androidprojectiti.database.relations

import androidx.room.Entity
import com.example.androidprojectiti.dto.MealResponse.Meal

// We will link the two tables together using their primary keys
@Entity(primaryKeys = ["email", "meal"])
data class UserFavorites(
    val email : String,
    val meal : Meal
)
