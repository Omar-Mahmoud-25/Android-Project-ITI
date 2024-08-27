package com.example.androidprojectiti.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidprojectiti.dto.MealResponse.Meal

interface MealDao {

    @Query("SELECT * FROM Meal")
    fun getAllLocalMeals() : List<Meal>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert (meal: Meal)


    @Update
    suspend fun update (meal: Meal)


    @Delete
    suspend fun delete (meal: Meal)
}