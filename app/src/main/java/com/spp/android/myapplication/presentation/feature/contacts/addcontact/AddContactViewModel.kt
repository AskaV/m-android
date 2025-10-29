package com.spp.android.myapplication.presentation.feature.contacts.addcontact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactDetails
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
        val nextId = (repository.current().maxOfOrNull { it.id } ?: 0) + 1
        val details = ContactDetails(
            id = nextId,
            name = _state.value.username,
            career = _state.value.career,
            phone = _state.value.phone,
            email = _state.value.email,
            address = _state.value.address,
            dateOfBirth = _state.value.dateOfBirth
        )

        viewModelScope.launch {
            repository.addContact(details)
            sendEffect(Effect.ShowMessage("Contact saved"))
            sendEffect(Effect.NavigateBack)
        }
    }


    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}