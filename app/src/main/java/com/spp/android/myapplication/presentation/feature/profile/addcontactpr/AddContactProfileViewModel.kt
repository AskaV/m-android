package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactProfileViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AddContactProfileContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    private var loadedContact: Contact? = null

    fun onEvent(event: Event) {
        when (event) {
            is Event.Load -> load(event.id)
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.MessageClicked -> sendEffect(Effect.ShowMessage("Open chat"))
            is Event.AddToContactsClicked -> addToContacts()
            is Event.ErrorShown -> _state.update { it.copy(error = "") }
        }
    }

    private fun load(id: Int) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = "") }

        val contact = contactsRepository.findContactById(id)
        loadedContact = contact

        val isInMy = contactsRepository.apiMyContacts.first().any { it.id == id }

        _state.update {
            it.copy(
                id = id,
                name = contact?.name.orEmpty(),
                linePrimary = contact?.subtitle.orEmpty(),
                lineSecondary = "",
                avatarPath = contact?.avatarUrl,
                isInMyContacts = isInMy,
                isLoading = false,
            )
        }
    }

    private fun addToContacts() = viewModelScope.launch {
        val c = loadedContact
        if (c == null) {
            sendEffect(Effect.ShowMessage("Contact not loaded"))
            return@launch
        }

        contactsRepository.addContactOfflineFirst(c).onSuccess {
            _state.update { it.copy(isInMyContacts = true) }
            sendEffect(Effect.ShowMessage("Added to contacts"))
        }.onFailure {
            sendEffect(Effect.ShowMessage("Failed to add"))
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch { _effect.send(effect) }
}