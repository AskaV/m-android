package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.dataSource.contact.ContactDataSource
import com.spp.android.myapplication.data.remote.api.ContactsApi
import com.spp.android.myapplication.data.remote.dto.AddContactBody
import com.spp.android.myapplication.data.remote.dto.UserDto
import com.spp.android.myapplication.data.remote.dto.toContact
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.data.storage.ContactsPreferences
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject

class ContactsRepositoryImpl
@Inject constructor(
    private val contactDataSource: ContactDataSource,
    private val contactsPreferences: ContactsPreferences,
    private val authPreferences: AuthPreferences,
    private val api: ContactsApi

) : ContactsRepository {
    override suspend fun loadContactsPhone(): List<Contact> {
        val systemContacts = contactDataSource.fetchContacts()

        val avatarMap = contactsPreferences.phonebookAvatarCache.first()
            .associateBy({ it.id }, { it.avatarUrl })

        var changed = false
        val updatedCache = contactsPreferences.phonebookAvatarCache.first().toMutableList()

        val result = systemContacts.map { c ->
            val cached = avatarMap[c.id]
            val finalAvatar = when {
                !cached.isNullOrBlank() -> cached
                !c.avatarUrl.isNullOrBlank() -> c.avatarUrl
                else -> {
                    val generated = generateAvatarUrl(c.id)
                    upsertAvatarCache(updatedCache, c.id, generated)
                    changed = true
                    generated
                }
            }
            c.copy(avatarUrl = finalAvatar)
        }

        if (changed) {
            contactsPreferences.savePhonebookAvatarCache(updatedCache.distinctBy { it.id })
        }

        return result
    }


    override suspend fun loadContactsLocal(): List<Contact> {
        return contactsPreferences.localAddedContacts.first()
    }


    override suspend fun addContactLocal(contact: Contact) {
        val current = contactsPreferences.localAddedContacts.first().toMutableList()
        val idx = current.indexOfFirst { it.id == contact.id }

        if (idx >= 0) {
            current[idx] = mergeKeepTextAndAvatar(current[idx], contact)
        } else {
            current.add(contact)
        }

        contactsPreferences.saveLocalAddedContacts(current.distinctBy { it.id })
    }

    override suspend fun deleteContactLocal(contactId: Int): Boolean {
        val current = contactsPreferences.localAddedContacts.first()
        if (current.none { it.id == contactId }) return false

        val updated = current.filterNot { it.id == contactId }
        contactsPreferences.saveLocalAddedContacts(updated)
        return true
    }


    override suspend fun setContactAvatar(contactId: Int, avatarUrl: String?) {
        val current = contactsPreferences.phonebookAvatarCache.first().toMutableList()
        upsertAvatarCache(current, contactId, avatarUrl)
        contactsPreferences.savePhonebookAvatarCache(current.distinctBy { it.id })
    }

    override suspend fun getUserContactsRemote(): List<Contact> {
        val userId = authPreferences.getUserId()
        val token = authPreferences.accessToken.first()

        val resp = api.getContacts(userId, "Bearer $token")
        if (!resp.isSuccessful) {
            val raw = resp.errorBody()?.string()
            throw Exception(parseErrorMessage(raw, resp.code()))
        }

        val body = resp.body()
        if (body?.status != "success" || body.data == null) {
            throw Exception(body?.message ?: "Unknown error")
        }

        return body.data.contacts.map { it.toContact() }.distinctBy { it.id }
    }


    override suspend fun addUserContactRemote(contactId: Int): Result<List<UserDto>> {
        return runCatching {
            val userId = authPreferences.getUserId()
            val token = authPreferences.accessToken.first()

            val resp =
                api.addContact(userId, "Bearer $token", AddContactBody(contactId = contactId))
            if (!resp.isSuccessful) {
                val raw = resp.errorBody()?.string()
                throw Exception(parseErrorMessage(raw, resp.code()))
            }

            val body = resp.body()
            if (body?.status != "success" || body.data == null) {
                throw Exception(body?.message ?: "Unknown error")
            }

            body.data.contacts
        }
    }


    override suspend fun deleteUserContactRemote(contactId: Int): Result<List<UserDto>> {
        return runCatching {
            val userId = authPreferences.getUserId()
            val token = authPreferences.accessToken.first()

            val resp = api.deleteContact(userId, contactId, "Bearer $token")
            if (!resp.isSuccessful) {
                val raw = resp.errorBody()?.string()
                throw Exception(parseErrorMessage(raw, resp.code()))
            }

            val body = resp.body()
            if (body?.status != "success" || body.data == null) {
                throw Exception(body?.message ?: "Unknown error")
            }

            body.data.contacts
        }
    }
}

private fun upsertAvatarCache(list: MutableList<Contact>, contactId: Int, avatarUrl: String?) {
    val idx = list.indexOfFirst { it.id == contactId }
    if (idx >= 0) {
        val old = list[idx]
        list[idx] = old.copy(avatarUrl = avatarUrl)
    } else {
        list.add(Contact(id = contactId, name = "", subtitle = "", avatarUrl = avatarUrl))
    }
}


private fun mergeKeepTextAndAvatar(old: Contact, fresh: Contact): Contact {
    return old.copy(
        name = fresh.name.takeIf { it.isNotBlank() } ?: old.name,
        subtitle = fresh.subtitle.takeIf { it.isNotBlank() } ?: old.subtitle,
        avatarUrl = fresh.avatarUrl ?: old.avatarUrl,
        transitionName = fresh.transitionName ?: old.transitionName,
    )
}

private fun parseErrorMessage(raw: String?, code: Int): String {
    if (raw.isNullOrBlank()) return "HTTP $code"
    return try {
        val obj = JSONObject(raw)
        obj.optString("message").takeIf { it.isNotBlank() } ?: raw
    } catch (_: Exception) {
        raw
    }
}

private fun generateAvatarUrl(contactId: Int): String =
    "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_$contactId"