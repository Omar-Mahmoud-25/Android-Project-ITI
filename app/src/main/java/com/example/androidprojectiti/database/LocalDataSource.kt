package com.example.androidprojectiti.database

import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal

interface LocalDataSource {

    suspend fun getAllLocalUsers() : List<User>

    suspend fun getUserFavoriteMeals(email: String) : List<Meal>

    suspend fun getUser(email: String): List<User>

    suspend fun insertUser (user: User)

    suspend fun insertMealToFav (fav: UserFavorites)

    suspend fun updateUser (user: User)

    suspend fun deleteUser (user: User)

    suspend fun deleteMealFromFav (fav: UserFavorites)
}