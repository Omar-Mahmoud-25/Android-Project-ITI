package com.example.androidprojectiti.database

import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserMeals
import com.example.androidprojectiti.dto.MealResponse.Meal

interface LocalDataSource {

    fun getAllLocalUsers() : List<User>

    fun getUserMeals(userId : Int) : UserMeals

    suspend fun insertUser (user: User)

    suspend fun insertMeal (meal: Meal)

    suspend fun updateUser (user: User)

    suspend fun deleteUser (user: User)

    suspend fun deleteMeal (meal: Meal)
}