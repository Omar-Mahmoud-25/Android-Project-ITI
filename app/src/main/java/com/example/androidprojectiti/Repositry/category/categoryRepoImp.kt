package com.example.androidprojectiti.Repositry.category

import com.example.androidprojectiti.dto.CategoryResponse.CategoryModel
import com.example.androidprojectiti.network.RemoteDataSource
import retrofit2.Response

class categoryRepoImp (
    private var remoteDataSource: RemoteDataSource

    ) : categoryRepo {
    override suspend fun getAllCategories(): Response<CategoryModel> {
        return remoteDataSource.getAllCategoriesFromRemoteDataSource()
    }
}
