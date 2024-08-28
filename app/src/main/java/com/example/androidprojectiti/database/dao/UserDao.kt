package com.example.androidprojectiti.database.dao

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidprojectiti.database.entity.User
import com.example.androidprojectiti.database.relations.UserWithMealsRef
import com.example.androidprojectiti.dto.MealResponse.Meal

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    suspend fun getAllLocalUsers() : List<User>


    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUser(email: String): List<User>
//    suspend fun getUserMeals(email: String) : UserWithMealsRef

//    @Query("SELECT idMeal FROM UserWithMealsRef WHERE email = :email")
//    suspend fun getUserFavoriteMeals(email: String): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser (user: User)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal (meal: Meal)


    @Update
    suspend fun updateUser (user: User)


    @Delete
    suspend fun deleteUser (user: User)


    @Delete
    suspend fun deleteMeal (meal: Meal)
}