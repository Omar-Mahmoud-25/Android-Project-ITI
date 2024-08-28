package com.example.androidprojectiti.database.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.dto.MealResponse.Meal

data class MealWithUsers(
    @Embedded val meal: Meal,
    @Relation(
        parentColumn = "idMeal",
        entityColumn = "email",
        associateBy = Junction(UserWithMealsRef::class)
    )
    val users : List<User>
)
