package com.example.androidprojectiti.database.relations

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.dto.MealResponse.Meal


// user contains a lot of meals
@Entity
data class UserMeals(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userIdFromMeal"
    )
    val meals : List<Meal>
)
