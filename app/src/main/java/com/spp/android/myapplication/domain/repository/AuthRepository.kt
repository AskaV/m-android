package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import java.io.File

interface AuthRepository {
    suspend fun register(
        email: String,
        password: String,
        name: String?,
        phone: String?,
        imageFile: File?,
    ): Result<AuthDataDto>
}
