package com.example.androidprojectiti.database

import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserWithMealsRef
import com.example.androidprojectiti.dto.MealResponse.Meal

interface LocalDataSource {

    fun getAllLocalUsers() : List<User>

    fun getUserMeals(email: String) : UserWithMealsRef

    suspend fun insertUser (user: User)

    suspend fun insertMeal (meal: Meal)

    suspend fun updateUser (user: User)

    suspend fun deleteUser (user: User)

    suspend fun deleteMeal (meal: Meal)
}