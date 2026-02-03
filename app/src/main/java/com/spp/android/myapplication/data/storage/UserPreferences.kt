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
            prefs[Companion.rememberMe] ?: false
        }

    override val savedEmail: Flow<String> =
        dataStore.data.map { prefs ->
            prefs[userEmail] ?: ""
        }


    override val userProfile: Flow<UserProfile?> =
        dataStore.data.map { prefs ->
            val username = prefs[profileUsername] ?: return@map null

            UserProfile(
                username = username,
                career = prefs[profileCareer] ?: "",
                phone = prefs[profilePhone] ?: "",
                address = prefs[profileAddress] ?: "",
                birthdate = prefs[profileBirthdate] ?: "",
                avatarPath = prefs[profileAvatar],
                isCompleted = prefs[profileCompleted] ?: false,
            )
        }

    override suspend fun saveUser(
        email: String,
        remember: Boolean,
    ) {
        dataStore.edit { prefs ->
            prefs[Companion.rememberMe] = remember
            if (remember) {
                prefs[userEmail] = email
            } else {
                prefs.remove(userEmail)
            }
        }
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { prefs ->
            prefs[profileUsername] = profile.username
            prefs[profileCareer] = profile.career
            prefs[profilePhone] = profile.phone
            prefs[profileAddress] = profile.address
            prefs[profileBirthdate] = profile.birthdate

            if (profile.avatarPath != null) {
                prefs[profileAvatar] = profile.avatarPath
            } else {
                prefs.remove(profileAvatar)
            }

            prefs[profileCompleted] = profile.isCompleted
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val Context.userPrefs by preferencesDataStore("user_prefs")

        private val rememberMe = booleanPreferencesKey("remember_me")
        private val userEmail = stringPreferencesKey("user_email")

        private val profileUsername = stringPreferencesKey("profile_username")
        private val profileCareer = stringPreferencesKey("profile_career")
        private val profilePhone = stringPreferencesKey("profile_phone")
        private val profileAddress = stringPreferencesKey("profile_address")
        private val profileBirthdate = stringPreferencesKey("profile_birthdate")
        private val profileAvatar = stringPreferencesKey("profile_avatar")
        private val profileCompleted = booleanPreferencesKey("profile_completed")
    }
}