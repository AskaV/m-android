package com.spp.android.myapplication.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthPreferences
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        private val ds = context.authDataStore

        private object Keys {
            val ACCESS = stringPreferencesKey("access_token")
            val REFRESH = stringPreferencesKey("refresh_token")
        }

        val accessToken: Flow<String> = ds.data.map { it[Keys.ACCESS].orEmpty() }
        val refreshToken: Flow<String> = ds.data.map { it[Keys.REFRESH].orEmpty() }

        suspend fun saveTokens(
            access: String,
            refresh: String,
        ) {
            ds.edit { prefs ->
                prefs[Keys.ACCESS] = access
                prefs[Keys.REFRESH] = refresh
            }
        }

        suspend fun clear() {
            ds.edit { it.clear() }
        }
    }
