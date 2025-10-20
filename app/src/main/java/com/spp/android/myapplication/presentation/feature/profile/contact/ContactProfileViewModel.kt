package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.contact.ContactProfileContract.Event
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactProfileViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ContactProfileContract.State())
    val state: StateFlow<ContactProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.Load -> load(event.contactId)
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)

            is Event.MessageClicked -> _state.value.contactId.takeIf { it.isNotBlank() }?.let {
                sendEffect(Effect.OpenChat(it))
            }

            is Event.AddClicked -> addContact()
            is Event.ErrorShown -> _state.update { it.copy(errorKey = null) }
        }
    }

    private fun load(id: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, contactId = id, errorKey = null) }

        runCatching {
            StubContact(
                id = id,
                name = "Lucile Alvarado",
                linePrimary = "Product Designer",
                lineSecondary = "New York, USA",
                hasSocial = true
            )
        }.onSuccess { c ->
            _state.update {
                it.copy(
                    name = c.name,
                    linePrimary = c.linePrimary,
                    lineSecondary = c.lineSecondary,
                    hasSocial = c.hasSocial,
                )
            }
        }.onFailure {
            _state.update { it.copy(isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR) }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_LOAD_FAILED))
        }
    }

    private fun addContact() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        runCatching { true }.onSuccess {
            _state.update { it.copy(isLoading = false, hasSocial = true) }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACT_ADDED))
        }.onFailure {
            _state.update {
                it.copy(
                    isLoading = false, errorKey = AppText.OtherInfo.UNKNOWN_ERROR
                )
            }
            sendEffect(Effect.ShowMessage(AppText.OtherInfo.FAILED_TO_ADD_CONTACT))
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private data class StubContact(
        val id: String,
        val name: String,
        val linePrimary: String,
        val lineSecondary: String,
        val hasSocial: Boolean
    )
}