package com.example.androidprojectiti.database.relations

import androidx.room.Entity

// We will link the two tables together using their primary keys
@Entity(primaryKeys = ["email", "idMeal"])
data class UserFavorites(
    val email : String,
    val idMeal : String
)
