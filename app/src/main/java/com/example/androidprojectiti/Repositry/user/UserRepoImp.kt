package com.example.androidprojectiti.Repositry.user

import com.example.androidprojectiti.database.LocalDataSource
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal

class UserRepoImp (private val _localDataSource: LocalDataSource) : UserRepo {
    override suspend fun getAllLocalUsers(): List<User> {
        return _localDataSource.getAllLocalUsers()
    }

    override suspend fun getUserFavoriteMeals(email: String): List<String> {
        return _localDataSource.getUserFavoriteMeals(email)
    }

    override suspend fun getUser(email: String): List<User> {
        return _localDataSource.getUser(email)
    }

    override suspend fun insertUser(user: User) {
        return _localDataSource.insertUser(user)
    }


    override suspend fun updateUser(user: User) {
        return _localDataSource.updateUser(user)
    }

    override suspend fun insertMealToFav(fav: UserFavorites) {
        return _localDataSource.insertMealToFav(fav)
    }

    override suspend fun deleteMealFromFav(fav: UserFavorites) {
        return _localDataSource.deleteMealFromFav(fav)
    }

}