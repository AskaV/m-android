package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.Event
import com.spp.android.myapplication.presentation.feature.contacts.addcontact.AddContactContract.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val repository: ContactsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.BackClicked -> sendEffect(Effect.NavigateBack)
            is Event.AvatarClicked -> sendEffect(Effect.ShowMessage("Avatar picker clicked"))

            is Event.UsernameChanged -> _state.update { it.copy(username = event.value) }
            is Event.CareerChanged -> _state.update { it.copy(career = event.value) }
            is Event.EmailChanged -> _state.update { it.copy(email = event.value) }
            is Event.PhoneChanged -> _state.update { it.copy(phone = event.value) }
            is Event.AddressChanged -> _state.update { it.copy(address = event.value) }
            is Event.DateOfBirthChanged -> _state.update { it.copy(dateOfBirth = event.value) }

            is Event.SaveClicked -> save()
            is Event.ErrorShown -> _state.update { it.copy() }
        }
    }

    private fun save() {
        val usernameError = if (_state.value.username.isBlank()) "Required" else ""
        val emailError = if (_state.value.email.isBlank()) "" else ""
        val phoneError = ""

        _state.update { it.copy(usernameError = usernameError, emailError = emailError, phoneError = phoneError) }
        if (usernameError.isNotEmpty() || emailError.isNotEmpty() || phoneError.isNotEmpty()) return

        val nextId = (repository.current().maxOfOrNull { it.id } ?: 0) + 1
        val name = _state.value.username
        val subtitle = if (_state.value.phone.isNotBlank()) _state.value.phone else _state.value.career

        val contact = ContactUi(
            id = nextId,
            name = name,
            subtitle = subtitle,
            avatarUrl = null,
            transitionName = "contact_$nextId"
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            repository.addContact(contact)
            _state.update { it.copy(isSaving = false) }
            sendEffect(Effect.ShowMessage("Contact saved"))
            sendEffect(Effect.NavigateBack)
        }
    }


    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}