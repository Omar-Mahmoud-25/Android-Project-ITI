package com.example.androidprojectiti.repositories.meal

import com.example.androidprojectiti.dto.MealResponse.MealModel

import retrofit2.Response

interface mealRepo {
    suspend fun getAllMeals(letter:Char): Response<MealModel>
    suspend fun getRandomMeal():Response<MealModel>
    suspend fun getMealById(id:String):Response<MealModel>
    suspend fun getMealByName(name:String):Response<MealModel>
    suspend fun getMealsByCategory(category: String):Response<MealModel>
}