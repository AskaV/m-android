package com.spp.android.myapplication.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.spp.android.myapplication.domain.model.UserProfile
import com.spp.android.myapplication.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : LocalStorage {

    override val rememberMe: Flow<Boolean> =
        dataStore.data.map { prefs ->
            prefs[REMEMBER_ME] ?: false
        }

    override val savedEmail: Flow<String> =
        dataStore.data.map { prefs ->
            prefs[USER_EMAIL] ?: ""
        }


    override val userProfile: Flow<UserProfile?> =
        dataStore.data.map { prefs ->
            val username = prefs[PROFILE_USERNAME] ?: return@map null

            UserProfile(
                username = username,
                career = prefs[PROFILE_CAREER] ?: "",
                phone = prefs[PROFILE_PHONE] ?: "",
                address = prefs[PROFILE_ADDRESS] ?: "",
                birthdate = prefs[PROFILE_BIRTHDATE] ?: "",
                avatarPath = prefs[PROFILE_AVATAR],
                isCompleted = prefs[PROFILE_COMPLETED] ?: false,
            )
        }

    override suspend fun saveUser(
        email: String,
        remember: Boolean,
    ) {
        dataStore.edit { prefs ->
            prefs[REMEMBER_ME] = remember
            if (remember) {
                prefs[USER_EMAIL] = email
            } else {
                prefs.remove(USER_EMAIL)
            }
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[PROFILE_USERNAME] = profile.username
            prefs[PROFILE_CAREER] = profile.career
            prefs[PROFILE_PHONE] = profile.phone
            prefs[PROFILE_ADDRESS] = profile.address
            prefs[PROFILE_BIRTHDATE] = profile.birthdate

            if (profile.avatarPath != null) {
                prefs[PROFILE_AVATAR] = profile.avatarPath
            } else {
                prefs.remove(PROFILE_AVATAR)
            }

            prefs[PROFILE_COMPLETED] = profile.isCompleted
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val Context.userPrefs by preferencesDataStore("user_prefs")

        private val REMEMBER_ME = booleanPreferencesKey("remember_me")
        private val USER_EMAIL = stringPreferencesKey("user_email")

        private val PROFILE_USERNAME = stringPreferencesKey("profile_username")
        private val PROFILE_CAREER = stringPreferencesKey("profile_career")
        private val PROFILE_PHONE = stringPreferencesKey("profile_phone")
        private val PROFILE_ADDRESS = stringPreferencesKey("profile_address")
        private val PROFILE_BIRTHDATE = stringPreferencesKey("profile_birthdate")
        private val PROFILE_AVATAR = stringPreferencesKey("profile_avatar")
        private val PROFILE_COMPLETED = booleanPreferencesKey("profile_completed")
    }
}