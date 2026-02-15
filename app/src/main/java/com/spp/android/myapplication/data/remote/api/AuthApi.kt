package com.spp.android.myapplication.data.remote.api

import com.spp.android.myapplication.data.remote.dto.ApiResponse
import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {
    @Multipart
    @POST("users")
    suspend fun createUser(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part image: MultipartBody.Part? = null,
    ): Response<ApiResponse<AuthDataDto>>
}
