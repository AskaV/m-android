package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.remote.api.AuthApi
import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import com.spp.android.myapplication.data.remote.util.toImagePart
import com.spp.android.myapplication.data.remote.util.toPart
import com.spp.android.myapplication.domain.repository.AuthRepository
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApi,
    ) : AuthRepository {
        override suspend fun register(
            email: String,
            password: String,
            name: String?,
            phone: String?,
            imageFile: File?,
        ): Result<AuthDataDto> =
            runCatching {
                val resp =
                    api.createUser(
                        email = email.trim().toPart(),
                        password = password.toPart(),
                        name = name?.takeIf { it.isNotBlank() }?.toPart(),
                        phone = phone?.takeIf { it.isNotBlank() }?.toPart(),
                        image = imageFile?.takeIf { it.exists() }?.toImagePart("image"),
                    )

                if (!resp.isSuccessful) {
                    val raw = resp.errorBody()?.string()
                    throw Exception(parseErrorMessage(raw, resp.code()))
                }

                val body = resp.body()
                if (body?.status != "success" || body.data == null) {
                    throw Exception(body?.message ?: "Unknown error")
                }

                body.data
            }
    }

private fun parseErrorMessage(
    raw: String?,
    code: Int,
): String {
    if (raw.isNullOrBlank()) return "HTTP $code"

    return try {
        val obj = org.json.JSONObject(raw)
        obj.optString("message").takeIf { it.isNotBlank() } ?: raw
    } catch (_: Exception) {
        raw
    }
}
