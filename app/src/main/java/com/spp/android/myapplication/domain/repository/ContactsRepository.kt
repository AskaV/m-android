package com.spp.android.myapplication.domain.repository

import com.spp.android.myapplication.domain.model.Contact

interface ContactsRepository {
    fun loadContacts(): List<Contact>
}