package com.example.androidprojectiti.repositories.category

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import retrofit2.Response

interface categoryRepo {
    suspend fun getAllCategories(): Response<CategoryModel>
}