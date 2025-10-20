package com.spp.android.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userPrefs by preferencesDataStore("user_prefs")

object UserPreferencesKeys {
    val REMEMBER_ME = booleanPreferencesKey("remember_me")
    val USER_EMAIL = stringPreferencesKey("user_email")
}

class UserPreferences(private val context: Context) {

    val rememberMe: Flow<Boolean> = context.userPrefs.data.map { prefs ->
        prefs[UserPreferencesKeys.REMEMBER_ME] ?: false
    }

    val savedEmail: Flow<String> = context.userPrefs.data.map { prefs ->
        prefs[UserPreferencesKeys.USER_EMAIL] ?: ""
    }

    suspend fun saveUser(email: String, remember: Boolean) {
        context.userPrefs.edit { prefs ->
            prefs[UserPreferencesKeys.REMEMBER_ME] = remember
            if (remember) {
                prefs[UserPreferencesKeys.USER_EMAIL] = email
            } else {
                prefs.remove(UserPreferencesKeys.USER_EMAIL)
            }
        }
    }

    suspend fun clear() {
        context.userPrefs.edit { it.clear() }
    }
}