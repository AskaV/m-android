package com.spp.android.myapplication.data.remote.dto

data class AuthDataDto(
    val user: UserDto,
    val accessToken: String,
    val refreshToken: String,
)
