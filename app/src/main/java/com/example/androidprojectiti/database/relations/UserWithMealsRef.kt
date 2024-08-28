package com.example.androidprojectiti.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.dto.MealResponse.Meal

// We will link the two tables together using their primary keys
@Entity(primaryKeys = ["email", "idMeal"])
data class UserWithMealsRef(
    val email : String,
    val idMeal : String
)
