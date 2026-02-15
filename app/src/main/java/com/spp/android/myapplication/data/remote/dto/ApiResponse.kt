package com.spp.android.myapplication.data.remote.dto

data class ApiResponse<T>(
    val status: String,
    val code: Int,
    val message: String? = null,
    val data: T? = null,
)
