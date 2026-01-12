package com.spp.android.myapplication.domain.storage

import com.spp.android.myapplication.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    val rememberMe: Flow<Boolean>
    val savedEmail: Flow<String>

    suspend fun saveUser(
        email: String,
        remember: Boolean,
    )

    val userProfile: Flow<UserProfile?>
    suspend fun saveUserProfile(profile: UserProfile)

    suspend fun clear()
}
