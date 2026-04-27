package com.spp.android.myapplication.data.remote.api

import com.spp.android.myapplication.data.remote.dto.ApiResponse
import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import com.spp.android.myapplication.data.remote.dto.EditUserBody
import com.spp.android.myapplication.data.remote.dto.EditUserDataDto
import com.spp.android.myapplication.data.remote.dto.LoginBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApi {
    @Multipart
    @POST("users")
    suspend fun registerUser(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody? = null,
        @Part("phone") phone: RequestBody? = null,
        @Part image: MultipartBody.Part? = null,
    ): Response<ApiResponse<AuthDataDto>>

    @POST("login")
    suspend fun login(
        @Body body: LoginBody,
    ): Response<ApiResponse<AuthDataDto>>

    @PUT("users/{id}")
    suspend fun editUser(
        @Path("id") userId: Int,
        @Header("Authorization") bearer: String,
        @Body body: EditUserBody,
    ): Response<ApiResponse<EditUserDataDto>>
}
