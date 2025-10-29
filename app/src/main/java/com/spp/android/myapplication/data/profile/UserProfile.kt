package com.spp.android.myapplication.data.profile

data class UserProfile(
    val name: String = "",
    val career: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = "",
    val avatarUrl: String? = null,
    val isPasswordSaved : Boolean = false
)