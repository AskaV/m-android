package com.spp.android.myapplication.data.remote.dto

data class UserDto(
    val id: Int,
    val email: String,
    val name: String? = null,
    val phone: String? = null,
)
