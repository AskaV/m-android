package com.spp.android.myapplication.data.remote.api

import com.spp.android.myapplication.data.remote.dto.AddContactBody
import com.spp.android.myapplication.data.remote.dto.ApiResponse
import com.spp.android.myapplication.data.remote.dto.ContactsDataDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactsApi {

    @GET("users/{userId}/contacts")
    suspend fun getContacts(
        @Path("userId") userId: Int,
        @Header("Authorization") bearer: String,
    ): Response<ApiResponse<ContactsDataDto>>

    @PUT("users/{userId}/contacts")
    suspend fun addContact(
        @Path("userId") userId: Int,
        @Header("Authorization") bearer: String,
        @Body body: AddContactBody,
    ): Response<ApiResponse<ContactsDataDto>>

    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact(
        @Path("userId") userId: Int,
        @Path("contactId") contactId: Int,
        @Header("Authorization") bearer: String,
    ): Response<ApiResponse<ContactsDataDto>>
}