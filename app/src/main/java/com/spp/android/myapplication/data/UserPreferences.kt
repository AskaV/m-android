package com.spp.android.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

private const val EMAIL = "email"

object UserPreferences {

    private val EMAIL_KEY = stringPreferencesKey(EMAIL)

    suspend fun saveEmail(context: Context, email: String) {
        context.dataStore.edit { prefs ->
            prefs[EMAIL_KEY] = email
        }
    }

    suspend fun getEmail(context: Context): String? {
        return context.dataStore.data.map { prefs ->
            prefs[EMAIL_KEY]
        }.first()
    }
}