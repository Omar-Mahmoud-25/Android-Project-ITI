package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.AreaResponse.AreaModel
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

    @GET("filter.php")
    suspend fun getMealsByCategory(@Query("c") category: String) : Response<MealModel>
    @GET("search.php")
    suspend fun getMealByName(@Query("s") name:String) : Response<MealModel>

    @GET("list.php")
    suspend fun getAreas(@Query("a") list:String ="list"):Response<AreaModel>
    @GET("filter.php")
    suspend fun getMealsByArea(@Query("a") area: String): Response<MealModel>
}