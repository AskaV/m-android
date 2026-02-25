package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.data.remote.dto.UserDto
import com.spp.android.myapplication.domain.model.Contact

interface ContactsRepository {
    suspend fun loadContactsPhone(): List<Contact>
    suspend fun loadContactsLocal(): List<Contact>
    suspend fun addContactLocal(contact: Contact)

    suspend fun deleteContactLocal(contactId: Int): Boolean

    suspend fun setContactAvatar(
        contactId: Int,
        avatarUrl: String?,
    )

    suspend fun getUserContactsRemote(): List<Contact>
    suspend fun addUserContactRemote(contactId: Int): Result<List<UserDto>>
    suspend fun deleteUserContactRemote(contactId: Int): Result<List<UserDto>>
}
