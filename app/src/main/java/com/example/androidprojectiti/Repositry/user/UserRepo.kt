package com.example.androidprojectiti.Repositry.user

import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal

interface UserRepo {
    suspend fun getAllLocalUsers() : List<User>

    suspend fun getUserFavoriteMeals(email: String) : List<Meal>

    suspend fun insertMealToFav(fav: UserFavorites)

    suspend fun deleteMealFromFav(fav: UserFavorites)

    suspend fun getUser(email: String): List<User>

    suspend fun insertUser (user: User)

    suspend fun updateUser (user: User)
}