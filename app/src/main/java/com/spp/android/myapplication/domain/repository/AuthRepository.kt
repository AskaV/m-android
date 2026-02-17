package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import com.spp.android.myapplication.data.remote.dto.UserDto

interface AuthRepository {
    suspend fun register(
        email: String,
        password: String,
    ): Result<AuthDataDto>

    suspend fun editUser(
        userId: Int,
        accessToken: String,
        name: String?,
        phone: String?,
        address: String? = null,
        career: String? = null,
        birthday: String? = null,
        facebook: String? = null,
        instagram: String? = null,
        twitter: String? = null,
        linkedin: String? = null,
    ): Result<UserDto>

    suspend fun login(
        email: String,
        password: String,
    ): Result<AuthDataDto>
}
