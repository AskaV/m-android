package com.spp.android.myapplication.domain.storage

import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    val rememberMe: Flow<Boolean>
    val savedEmail: Flow<String>

    suspend fun saveUser(
        email: String,
        remember: Boolean,
    )

    suspend fun clear()
}
