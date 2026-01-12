package com.spp.android.myapplication.domain.model

data class UserProfile(
    val username: String = "",
    val career: String = "",
    val phone: String = "",
    val address: String = "",
    val birthdate: String = "",
    val avatarPath: String? = null,
    val isCompleted: Boolean = false
)