package com.spp.android.myapplication.di

import android.content.ContentResolver
import android.content.Context
import com.spp.android.myapplication.data.dataSource.contact.ContactDataSource
import com.spp.android.myapplication.data.dataSource.contact.LocalContactDataSource
import com.spp.android.myapplication.data.repository.ContactsRepositoryImpl
import com.spp.android.myapplication.domain.repository.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ContactsBinds {

    @Binds
    @Singleton
    fun bindContactsRepository(
        contactsRepository: ContactsRepositoryImpl,
    ): ContactsRepository

    @Binds
    @Singleton
    fun bindContactDataSource(
        contactDataSource: LocalContactDataSource,
    ): ContactDataSource
}


@Module
@InstallIn(SingletonComponent::class)
object ContactsProvides {
    @Provides
    fun provideContentResolver(
        @ApplicationContext context: Context
    ): ContentResolver = context.contentResolver
}