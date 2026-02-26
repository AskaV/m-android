package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.dataSource.contact.ContactDataSource
import com.spp.android.myapplication.data.dataSource.contact.ContactsLocalDataSource
import com.spp.android.myapplication.data.remote.api.ContactsApi
import com.spp.android.myapplication.data.remote.api.UsersApi
import com.spp.android.myapplication.data.remote.dto.AddContactBody
import com.spp.android.myapplication.data.remote.dto.toContact
import com.spp.android.myapplication.data.storage.AuthPreferences
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.json.JSONObject
import javax.inject.Inject
import kotlin.math.abs

class ContactsRepositoryImpl @Inject constructor(
    private val contactDataSource: ContactDataSource,
    private val local: ContactsLocalDataSource,
    private val authPreferences: AuthPreferences,
    private val api: ContactsApi,
    private val usersApi: UsersApi,

    ) : ContactsRepository {

    companion object {
        private const val SHOW_PHONEBOOK = false  //false true

        private const val PHONEBOOK_ID_OFFSET = 1_000_000
    }

    override val apiAllUsers: Flow<List<Contact>> = local.apiAllUsersCache
    override val apiMyContacts: Flow<List<Contact>> = local.apiMyContactsCache
    override val localAdded: Flow<List<Contact>> = local.localAddedContacts
    private val phonebookCache = MutableStateFlow<List<Contact>>(emptyList())

    override suspend fun refreshMyContacts(): Result<Unit> = runCatching {
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

        val contacts = body.data.contacts.map { it.toContact() }.distinctBy { it.id }
        local.saveApiMyContactsCache(contacts)
    }

    override suspend fun refreshAllUsers(): Result<Unit> = runCatching {
        val token = authPreferences.accessToken.first()
        val bearer = "Bearer $token"

        val resp = usersApi.getAllUsers(bearer)
        if (!resp.isSuccessful) {
            val raw = resp.errorBody()?.string()
            throw Exception(parseErrorMessage(raw, resp.code()))
        }

        val body = resp.body()
        if (body?.status != "success" || body.data == null) {
            throw Exception(body?.message ?: "Unknown error")
        }

        val users = body.data.users.map { it.toContact() }.distinctBy { it.id }
        local.saveApiAllUsersCache(users)
    }

    override suspend fun addContactOfflineFirst(contact: Contact): Result<Unit> = runCatching {
        val current = local.apiMyContactsCache.first()
        local.saveApiMyContactsCache((current + contact).distinctBy { it.id })

        val userId = authPreferences.getUserId()
        val token = authPreferences.accessToken.first()

        val resp = api.addContact(userId, "Bearer $token", AddContactBody(contactId = contact.id))
        if (!resp.isSuccessful) {
            val raw = resp.errorBody()?.string()
            throw Exception(parseErrorMessage(raw, resp.code()))
        }

        val body = resp.body()
        if (body?.status != "success" || body.data == null) {
            throw Exception(body?.message ?: "Unknown error")
        }

        val serverMine = body.data.contacts.map { it.toContact() }.distinctBy { it.id }
        local.saveApiMyContactsCache(serverMine)
    }

    override suspend fun deleteContactOfflineFirst(contactId: Int): Result<Unit> = runCatching {
        val current = local.apiMyContactsCache.first()
        local.saveApiMyContactsCache(current.filterNot { it.id == contactId })

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

        val serverMine = body.data.contacts.map { it.toContact() }.distinctBy { it.id }
        local.saveApiMyContactsCache(serverMine)
    }

    override suspend fun loadContactsPhone(): List<Contact> {
        if (!SHOW_PHONEBOOK) return emptyList()

        val systemContacts = contactDataSource.fetchContacts()

        val cached = local.phonebookAvatarCache.first()
        val avatarMap = cached.associateBy({ it.id }, { it.avatarUrl })

        var changed = false
        val updatedCache = cached.toMutableList()

        val resultRaw = systemContacts.map { c ->
            val rawId = c.id
            val fromCache = avatarMap[rawId]
            val finalAvatar = when {
                !fromCache.isNullOrBlank() -> fromCache
                !c.avatarUrl.isNullOrBlank() -> c.avatarUrl
                else -> {
                    val generated = generateAvatarUrl(rawId)
                    upsertAvatarCache(updatedCache, rawId, generated)
                    changed = true
                    generated
                }
            }
            c.copy(avatarUrl = finalAvatar)
        }

        if (changed) {
            local.savePhonebookAvatarCache(updatedCache.distinctBy { it.id })
        }

        return resultRaw.map { it.copy(id = it.id + PHONEBOOK_ID_OFFSET) }
    }

    override val showPhonebook: Flow<Boolean> = flowOf(true)

    override suspend fun setShowPhonebook(value: Boolean) {}

    override val visibleContacts: Flow<List<Contact>> = combine(
        local.apiMyContactsCache,
        local.localAddedContacts,
        showPhonebook,
        phonebookCache,
    ) { apiMine, localAdded, showPb, phonebook ->

        val merged = if (showPb) apiMine + localAdded + phonebook
        else apiMine + localAdded

        merged.distinctBy { it.id }
    }

    override suspend fun refreshVisibleContacts(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun loadContactsLocal(): List<Contact> = local.localAddedContacts.first()

    override suspend fun addContactLocal(contact: Contact) {
        val current = local.localAddedContacts.first().toMutableList()
        val idx = current.indexOfFirst { it.id == contact.id }

        if (idx >= 0) current[idx] = mergeKeepTextAndAvatar(current[idx], contact)
        else current.add(contact)

        local.saveLocalAddedContacts(current.distinctBy { it.id })
    }

    override suspend fun deleteContactLocal(contactId: Int): Boolean {
        val current = local.localAddedContacts.first()
        if (current.none { it.id == contactId }) return false
        local.saveLocalAddedContacts(current.filterNot { it.id == contactId })
        return true
    }

    override suspend fun setContactAvatar(contactId: Int, avatarUrl: String?) {
        val rawId =
            if (contactId >= PHONEBOOK_ID_OFFSET) contactId - PHONEBOOK_ID_OFFSET else abs(contactId)

        val current = local.phonebookAvatarCache.first().toMutableList()
        upsertAvatarCache(current, rawId, avatarUrl)
        local.savePhonebookAvatarCache(current.distinctBy { it.id })
    }

    override suspend fun findContactById(id: Int): Contact? {
        if (id >= PHONEBOOK_ID_OFFSET) {
            val phone = loadContactsPhone()
            return phone.firstOrNull { it.id == id }
        }

        local.apiAllUsersCache.first().firstOrNull { it.id == id }?.let { return it }
        local.apiMyContactsCache.first().firstOrNull { it.id == id }?.let { return it }
        local.localAddedContacts.first().firstOrNull { it.id == id }?.let { return it }

        return loadContactsPhone().firstOrNull { it.id == id + PHONEBOOK_ID_OFFSET }
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

private fun mergeKeepTextAndAvatar(old: Contact, fresh: Contact): Contact = old.copy(
    name = fresh.name.takeIf { it.isNotBlank() } ?: old.name,
    subtitle = fresh.subtitle.takeIf { it.isNotBlank() } ?: old.subtitle,
    avatarUrl = fresh.avatarUrl ?: old.avatarUrl,
    transitionName = fresh.transitionName ?: old.transitionName,
)

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