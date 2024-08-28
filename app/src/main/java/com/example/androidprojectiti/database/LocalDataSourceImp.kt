package com.example.androidprojectiti.database

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Email
import com.example.androidprojectiti.database.dao.UserDao
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserWithMealsRef
import com.example.androidprojectiti.dto.MealResponse.Meal

class LocalDataSourceImp(context: Context) : LocalDataSource {

    private var dao : UserDao

    init {
        val db = ProductDatabase.getInstance(context)
        dao = db.UserDao()
    }


    override fun getAllLocalUsers(): List<User> {
        return dao.getAllLocalUsers()
    }

    override fun getUserMeals(email: String): UserWithMealsRef {
        return dao.getUserMeals(email)
    }

    override suspend fun insertUser(user: User) {
        dao.insertUser(user)
    }

    override suspend fun insertMeal(meal: Meal) {
       dao.insertMeal(meal)
    }

    override suspend fun updateUser(user: User) {
        dao.updateUser(user)
    }

    override suspend fun deleteUser(user: User) {
        dao.deleteUser(user)
    }

    override suspend fun deleteMeal(meal: Meal) {
        dao.deleteMeal(meal)
    }
}