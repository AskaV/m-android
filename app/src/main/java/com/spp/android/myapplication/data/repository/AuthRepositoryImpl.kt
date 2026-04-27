package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.remote.api.AuthApi
import com.spp.android.myapplication.data.remote.dto.AuthDataDto
import com.spp.android.myapplication.data.remote.dto.EditUserBody
import com.spp.android.myapplication.data.remote.dto.LoginBody
import com.spp.android.myapplication.data.remote.dto.UserDto
import com.spp.android.myapplication.data.remote.util.toPart
import com.spp.android.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val api: AuthApi,
    ) : AuthRepository {
        override suspend fun register(
            email: String,
            password: String,
        ): Result<AuthDataDto> =
            runCatching {
                val resp =
                    api.registerUser(
                        email = email.trim().toPart(),
                        password = password.toPart(),
                        name = null,
                        phone = null,
                        image = null,
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

        override suspend fun editUser(
            userId: Int,
            accessToken: String,
            name: String?,
            phone: String?,
            address: String?,
            career: String?,
            birthday: String?,
            facebook: String?,
            instagram: String?,
            twitter: String?,
            linkedin: String?,
        ): Result<UserDto> =
            runCatching {
                val resp =
                    api.editUser(
                        userId = userId,
                        bearer = "Bearer $accessToken",
                        body =
                            EditUserBody(
                                name = name?.takeIf { it.isNotBlank() },
                                phone = phone?.takeIf { it.isNotBlank() },
                                address = address?.takeIf { it.isNotBlank() },
                                career = career?.takeIf { it.isNotBlank() },
                                birthday = birthday?.takeIf { it.isNotBlank() },
                                facebook = facebook?.takeIf { it.isNotBlank() },
                                instagram = instagram?.takeIf { it.isNotBlank() },
                                twitter = twitter?.takeIf { it.isNotBlank() },
                                linkedin = linkedin?.takeIf { it.isNotBlank() },
                            ),
                    )

                if (!resp.isSuccessful) {
                    val raw = resp.errorBody()?.string()
                    throw Exception(parseErrorMessage(raw, resp.code()))
                }

                val body = resp.body()
                if (body?.status != "success" || body.data == null) {
                    throw Exception(body?.message ?: "Unknown error")
                }
                println("EDIT PUT response user = ${body.data.user}")
                body.data.user
            }

        override suspend fun login(
            email: String,
            password: String,
        ): Result<AuthDataDto> =
            runCatching {
                val response =
                    api.login(
                        LoginBody(
                            email = email.trim(),
                            password = password,
                        ),
                    )

                if (!response.isSuccessful) {
                    val raw = response.errorBody()?.string()
                    throw Exception(parseErrorMessage(raw, response.code()))
                }

                val body = response.body()
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
