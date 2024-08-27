package com.example.androidprojectiti.Repositry.meal

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.dto.MealResponse.MealModel
import retrofit2.Response

interface mealRepo {
    suspend fun getAllMeals(letter:Char): Response<MealModel>
    suspend fun getRandomMeal():Response<MealModel>
}