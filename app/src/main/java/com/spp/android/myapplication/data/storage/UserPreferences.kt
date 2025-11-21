package com.spp.android.myapplication.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.spp.android.myapplication.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences
    @Inject
    constructor(
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

        override suspend fun clear() {
            dataStore.edit { it.clear() }
        }

        companion object {
            private val Context.userPrefs by preferencesDataStore("user_prefs")
            private val REMEMBER_ME = booleanPreferencesKey("remember_me")
            private val USER_EMAIL = stringPreferencesKey("user_email")
        }
    }
