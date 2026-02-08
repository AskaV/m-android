package com.spp.android.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

object ThemePreferences {
    private val KEY_THEME =
        stringPreferencesKey("theme_pref") // "light" | "dark" | "colored" | "system"

    fun themeFlow(context: Context): Flow<String> = context.settingsDataStore.data.map { prefs -> prefs[KEY_THEME] ?: "system" }

    suspend fun setTheme(
        context: Context,
        theme: String,
    ) {
        context.settingsDataStore.edit { it[KEY_THEME] = theme }
    }
}
