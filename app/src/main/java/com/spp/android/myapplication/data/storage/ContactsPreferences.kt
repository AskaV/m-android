package com.spp.android.myapplication.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.spp.android.myapplication.domain.model.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContactsPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    private val CONTACTS_JSON = stringPreferencesKey("contacts_json")

    val contacts: Flow<List<Contact>> = dataStore.data.map { prefs ->
        val json = prefs[CONTACTS_JSON].orEmpty()
        decodeContacts(json)
    }

    suspend fun saveContacts(list: List<Contact>) {
        dataStore.edit { prefs ->
            val compact = list.filter { it.id != 0 && !it.avatarUrl.isNullOrBlank() }
                .map { it.copy(name = "", subtitle = "", transitionName = null) }

            prefs[CONTACTS_JSON] = encodeContacts(compact)
        }
    }

    private fun encodeContacts(list: List<Contact>): String {
        val arr = org.json.JSONArray()
        list.forEach { c ->
            val obj = org.json.JSONObject()
            obj.put("id", c.id)
            obj.put("avatarUrl", c.avatarUrl ?: "")
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun decodeContacts(json: String): List<Contact> {
        if (json.isBlank()) return emptyList()
        return runCatching {
            val arr = org.json.JSONArray(json)
            val out = ArrayList<Contact>(arr.length())
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val id = obj.optInt("id", 0)
                val avatarUrl = obj.optString("avatarUrl", "").takeIf { it.isNotBlank() }
                if (id != 0 && avatarUrl != null) {
                    out.add(Contact(id = id, name = "", subtitle = "", avatarUrl = avatarUrl))
                }
            }
            out
        }.getOrDefault(emptyList())
    }
}