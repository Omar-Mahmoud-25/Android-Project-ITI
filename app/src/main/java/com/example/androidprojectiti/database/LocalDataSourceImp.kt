package com.example.androidprojectiti.database

import android.content.Context
import com.example.androidprojectiti.database.dao.UserDao
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserFavorites
import com.example.androidprojectiti.dto.MealResponse.Meal

class LocalDataSourceImp(context: Context) : LocalDataSource {

    private var dao : UserDao

    init {
        val db = ProductDatabase.getInstance(context)
        dao = db.UserDao()
    }


    override suspend fun getAllLocalUsers(): List<User> {
        return dao.getAllLocalUsers()
    }

    override suspend fun getUserFavoriteMeals(email: String): List<Meal> {
        return dao.getUserFavoriteMeals(email)
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun getUser(email: String): List<User>{
        return dao.getUser(email)
    }

    override suspend fun insertMealToFav(fav: UserFavorites) {
       dao.insertMealToFav(fav)
    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    override suspend fun deleteMealFromFav(fav: UserFavorites) {
        dao.deleteMealFromFav(fav)
    }
}