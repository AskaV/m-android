package com.spp.android.myapplication.data.dataSource.contact

import com.spp.android.myapplication.domain.model.Contact

interface ContactDataSource {
    fun fetchContacts(): List<Contact>
}