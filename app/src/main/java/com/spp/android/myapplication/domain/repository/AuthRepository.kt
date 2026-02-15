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
    ): Result<UserDto>
}
