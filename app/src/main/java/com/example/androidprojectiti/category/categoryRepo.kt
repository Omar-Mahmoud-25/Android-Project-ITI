package com.example.androidprojectiti.category

import com.example.androidprojectiti.dto.CategoryModel
import retrofit2.Response

interface categoryRepo {
    suspend fun getAllCategories(): Response<CategoryModel>
}