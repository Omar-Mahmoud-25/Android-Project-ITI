package com.example.androidprojectiti.Repositry.Area

import com.example.androidprojectiti.dto.AreaResponse.Area
import com.example.androidprojectiti.dto.AreaResponse.AreaModel
import com.example.androidprojectiti.network.RemoteDataSource
import retrofit2.Response

class AreaRepoImp(private var remoteDataSource: RemoteDataSource):AreaRepo {
    override suspend fun getaLLAreas(list:String): Response<AreaModel> {
        return remoteDataSource.getAllAreasFromRemoteDataSource()
    }
}