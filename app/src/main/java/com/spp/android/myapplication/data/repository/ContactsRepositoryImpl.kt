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
        val stored = contactsPreferences.contacts.first()

        val overrides = stored.associateBy { it.id }.toMutableMap()

        val migrated = migrateAvatarStyle(overrides)
        if (migrated) {
            contactsPreferences.saveContacts(overrides.values.toList())
        }

        var changed = false

        val result = systemContacts.map { contact ->
            val overrideAvatar = overrides[contact.id]?.avatarUrl
            val systemAvatar = contact.avatarUrl

            val finalAvatar = when {
                !overrideAvatar.isNullOrBlank() -> overrideAvatar
                !systemAvatar.isNullOrBlank() -> systemAvatar
                else -> {
                    val generated = generateAvatarUrl(contact.id)
                    overrides[contact.id] = Contact(
                        id = contact.id,
                        name = "",
                        subtitle = "",
                        avatarUrl = generated,
                    )
                    changed = true
                    generated
                }
            }

            contact.copy(avatarUrl = finalAvatar)
        }

        if (changed) {
            contactsPreferences.saveContacts(overrides.values.toList())
        }

        val systemIds = systemContacts.map { it.id }.toHashSet()

        val locallyAdded = stored.filter { it.name.isNotBlank() || it.subtitle.isNotBlank() }
            .filter { it.id !in systemIds }

        return result + locallyAdded
    }

    override suspend fun addContactLocal(contact: Contact) {
        val final = contact.copy(
            avatarUrl = contact.avatarUrl.takeUnless { it.isNullOrBlank() }
                ?: "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_${contact.id}",
        )

        val current = contactsPreferences.contacts.first().toMutableList()
        val idx = current.indexOfFirst { it.id == final.id }
        if (idx >= 0) current[idx] = final else current.add(final)

        contactsPreferences.saveContacts(current)
    }

    override suspend fun deleteContactLocal(contactId: Int): Boolean {
        val stored = contactsPreferences.contacts.first()

        val target = stored.firstOrNull { it.id == contactId } ?: return false

        val isAdded = target.name.isNotBlank() || target.subtitle.isNotBlank()
        if (!isAdded) return false

        val updated = stored.filterNot { it.id == contactId }
        contactsPreferences.saveContacts(updated)
        return true
    }

    override suspend fun setContactAvatar(
        contactId: Int,
        avatarUrl: String?,
    ) {
        val current = contactsPreferences.contacts.first().toMutableList()

        val idx = current.indexOfFirst { it.id == contactId }
        if (idx >= 0) {
            current[idx] = current[idx].copy(avatarUrl = avatarUrl)
        } else {
            current.add(Contact(id = contactId, name = "", subtitle = "", avatarUrl = avatarUrl))
        }

        contactsPreferences.saveContacts(current)
    }



    override suspend fun getUserContactsRemote(): List<Contact> {
        val userId = authPreferences.getUserId()
        val token = authPreferences.accessToken.first()

        val resp = api.getContacts(userId = userId, bearer = "Bearer $token")
        if (!resp.isSuccessful) {
            val raw = resp.errorBody()?.string()
            throw Exception(parseErrorMessage(raw, resp.code()))
        }

        val body = resp.body()
        if (body?.status != "success" || body.data == null) {
            throw Exception(body?.message ?: "Unknown error")
        }

        return body.data.contacts
            .map { it.toContact() }
            .distinctBy { it.id }
    }

    override suspend fun addUserContactRemote(contactId: Int): Result<List<UserDto>> {
        return runCatching {
            val userId = authPreferences.getUserId()
            val token = authPreferences.accessToken.first()

            val resp = api.addContact(
                userId = userId,
                bearer = "Bearer $token",
                body = AddContactBody(contactId = contactId),
            )

            if (!resp.isSuccessful) {
                val raw = resp.errorBody()?.string()
                throw Exception(parseErrorMessage(raw, resp.code()))
            }

            val body = resp.body()
            if (body?.status != "success" || body.data == null) {
                throw Exception(body?.message ?: "Unknown error")
            }

            body.data.contacts // <-- List<UserDto>
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun deleteUserContactRemote(contactId: Int): Result<List<UserDto>> {
        return runCatching {
            val userId = authPreferences.getUserId()
            val token = authPreferences.accessToken.first()

            val resp = api.deleteContact(
                userId = userId,
                contactId = contactId,
                bearer = "Bearer $token",
            )

            if (!resp.isSuccessful) {
                val raw = resp.errorBody()?.string()
                throw Exception(parseErrorMessage(raw, resp.code()))
            }

            val body = resp.body()
            if (body?.status != "success" || body.data == null) {
                throw Exception(body?.message ?: "Unknown error")
            }

            body.data.contacts
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = { Result.failure(it) }
        )
    }
}

private fun parseErrorMessage(raw: String?, code: Int): String {
    if (raw.isNullOrBlank()) return "HTTP $code"
    return try {
        val obj = org.json.JSONObject(raw)
        obj.optString("message").takeIf { it.isNotBlank() } ?: raw
    } catch (_: Exception) {
        raw
    }
}

private fun generateAvatarUrl(contactId: Int): String =
    "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_$contactId"

private fun migrateAvatarStyle(overrides: MutableMap<Int, Contact>): Boolean {
    var changed = false

    overrides.entries.forEach { (id, c) ->
        val url = c.avatarUrl ?: return@forEach
        if (url.contains("/9.x/bottts/")) {
            overrides[id] = c.copy(
                avatarUrl = "https://api.dicebear.com/9.x/lorelei-neutral/png?seed=contact_$id",
            )
            changed = true
        }
    }

    return changed
}

