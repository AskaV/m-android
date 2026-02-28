package com.spp.android.myapplication.presentation.navigation

import androidx.lifecycle.ViewModel
import com.spp.android.myapplication.domain.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class DeepLinkResolverViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    suspend fun isInMyContacts(contactId: Int): Boolean {
        val apiIds = contactsRepository.apiMyContacts.first().map { it.id }.toSet()
        val localIds = contactsRepository.localAdded.first().map { it.id }.toSet()
        return contactId in apiIds || contactId in localIds
    }
}