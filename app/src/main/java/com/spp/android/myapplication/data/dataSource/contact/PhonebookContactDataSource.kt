package com.spp.android.myapplication.data.dataSource.contact

import android.content.ContentResolver
import android.provider.ContactsContract
import com.spp.android.myapplication.domain.model.Contact
import javax.inject.Inject

class PhonebookContactDataSource
    @Inject
    constructor(
        private val contentResolver: ContentResolver,
    ) : ContactDataSource {
        override fun fetchContacts(): List<Contact> {
            val projection =
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                )

            val cursor =
                contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection,
                    null,
                    null,
                    "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} COLLATE LOCALIZED ASC",
                )

            val result = mutableListOf<Contact>()
            val seen = HashSet<Int>()

            cursor?.use { c ->
                val idIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
                val nameIdx = c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)
                val numIdx = c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                while (c.moveToNext()) {
                    val id =
                        try {
                            c.getInt(idIdx)
                        } catch (e: Exception) {
                            c.getString(idIdx)?.toIntOrNull() ?: continue
                        }
                    if (!seen.add(id)) continue
                    val name = c.getString(nameIdx) ?: "No name"
                    val phone = c.getString(numIdx).orEmpty()
                    result.add(Contact(id, name, phone))
                }
            }
            return result
        }
    }
