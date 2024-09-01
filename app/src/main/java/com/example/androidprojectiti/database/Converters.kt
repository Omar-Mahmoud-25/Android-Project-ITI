package com.example.androidprojectiti.database

import androidx.room.TypeConverter
import com.example.androidprojectiti.dto.MealResponse.Meal
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromMealToString(meal: Meal): String {
        return Gson().toJson(meal)
    }

    @TypeConverter
    fun fromStringToDimensions(str: String): Meal {
        return Gson().fromJson(str, Meal::class.java)
    }
}