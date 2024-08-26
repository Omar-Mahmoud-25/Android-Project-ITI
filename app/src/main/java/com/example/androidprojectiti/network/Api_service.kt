package com.example.androidprojectiti.network

import com.example.androidprojectiti.dto.CategoryModel
import retrofit2.Response
import retrofit2.http.GET

interface Api_service {
    @GET("categories.php#")
    suspend fun getAllCategories(): Response<CategoryModel>
}