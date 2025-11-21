package com.spp.android.myapplication.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.spp.android.myapplication.data.storage.UserPreferences
import com.spp.android.myapplication.domain.storage.LocalStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageProvides {
    @Provides
    @Singleton
    fun provideUserPreferences(dataStore: DataStore<Preferences>): UserPreferences = UserPreferences(dataStore)

    @Provides
    @Singleton
    fun provideUserPrefs(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("user_prefs")
        }
}

@Module
@InstallIn(SingletonComponent::class)
interface StorageBinds {
    @Binds
    @Singleton
    fun bindUserPreferences(userPreferences: UserPreferences): LocalStorage
}
