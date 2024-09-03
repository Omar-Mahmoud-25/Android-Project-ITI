package com.example.androidprojectiti.Repositry.Area

import com.example.androidprojectiti.dto.AreaResponse.AreaModel
import retrofit2.Response

interface AreaRepo {
    suspend fun getaLLAreas(list:String="list"):Response<AreaModel>
}