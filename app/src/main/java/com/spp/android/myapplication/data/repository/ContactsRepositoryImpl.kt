package com.spp.android.myapplication.data.repository

import com.spp.android.myapplication.data.dataSource.contact.ContactDataSource
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import javax.inject.Inject

class ContactsRepositoryImpl
    @Inject
    constructor(
        private val contactDataSource: ContactDataSource,
    ) : ContactsRepository {
        override fun loadContacts(): List<Contact> = contactDataSource.fetchContacts()
    }
