package com.spp.android.myapplication.data.remote.api

import com.spp.android.myapplication.data.remote.dto.ApiResponse
import com.spp.android.myapplication.data.remote.dto.UsersDataDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.Response

interface UsersApi {
    @GET("users")
    suspend fun getAllUsers(
        @Header("Authorization") bearer: String,
    ): Response<ApiResponse<UsersDataDto>>
}