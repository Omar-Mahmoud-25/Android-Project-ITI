package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.dto.MealResponse.MealModel
import retrofit2.Response

interface RemoteDataSource {
    suspend fun getAllCategoriesFromRemoteDataSource(): Response<CategoryModel>
    suspend fun getMealsFromRemoteDataSource(letter:Char):Response<MealModel>
}