package com.spp.android.myapplication.presentation.feature.profile.contact

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.texts.AppText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactProfileViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ContactProfileContract.State())
    val state: StateFlow<ContactProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<ContactProfileContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: ContactProfileContract.Event) {
        when (event) {
            is ContactProfileContract.Event.Load -> load(event.contactId)
            ContactProfileContract.Event.BackClicked -> emit(ContactProfileContract.Effect.NavigateBack)

            ContactProfileContract.Event.MessageClicked -> _state.value.contactId.takeIf { it.isNotBlank() }
                ?.let {
                    emit(ContactProfileContract.Effect.OpenChat(it))
                }

            ContactProfileContract.Event.AddClicked -> addContact()
            ContactProfileContract.Event.ErrorShown -> _state.update { it.copy(error = null) }
        }
    }

    private fun load(id: String) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, contactId = id, error = null) }

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
                    isLoading = false
                )
            }
        }.onFailure { t ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
                )
            }
            emit(
                ContactProfileContract.Effect.ShowMessage(
                    AppText.OtherInfo.CONTACTS_LOAD_FAILED.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun addContact() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        runCatching {
            true
        }.onSuccess {
            _state.update { it.copy(isLoading = false, hasSocial = true) }
            emit(
                ContactProfileContract.Effect.ShowMessage(
                    AppText.OtherInfo.CONTACT_ADDED.text(
                        appContext
                    )
                )
            )
        }.onFailure { t ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
                )
            }
            emit(
                ContactProfileContract.Effect.ShowMessage(
                    AppText.OtherInfo.FAILED_TO_ADD_CONTACT.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun emit(effect: ContactProfileContract.Effect) = viewModelScope.launch {
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