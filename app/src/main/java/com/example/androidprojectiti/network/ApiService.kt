package com.example.androidprojectiti.network

import retrofit2.http.Query
import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.dto.MealResponse.MealModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("categories.php#")
    suspend fun getAllCategories(): Response<CategoryModel>
    @GET("search.php")
    suspend fun searchMeals(@Query("f") letter:Char): Response<MealModel>
    @GET("random.php")
    suspend fun getRandomMeal(): Response<MealModel>

    @GET("lookup.php")
    suspend fun getMealByID(@Query("i") id:String):Response<MealModel>
    @GET("search.php")
    suspend fun getMealByName(@Query("s") name:String) : Response<MealModel>

}