package com.example.androidprojectiti.Repositry.category

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import retrofit2.Response

interface categoryRepo {
    suspend fun getAllCategories(): Response<CategoryModel>
}