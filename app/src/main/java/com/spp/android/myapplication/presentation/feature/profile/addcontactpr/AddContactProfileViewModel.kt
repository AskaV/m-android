package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.repository.ContactsRepository
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.AddToContactsClicked
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.Load
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.MessageClicked
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun onEvent(event: AddContactProfileContract.Event) {
        when (event) {
            is Load -> load(event.id)
            is BackClicked -> sendEffect(Effect.NavigateBack)

            is MessageClicked ->
                sendEffect(
                    Effect.ShowMessage(
                        "Open chat",
                    ),
                )

            is AddToContactsClicked -> {
                sendEffect(Effect.ShowMessage("Added to contacts"))
                _state.update { it.copy(isInMyContacts = true) }
            }

            is ErrorShown -> _state.update { it.copy() }
        }
    }

    private fun load(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val contact = contactsRepository
                .loadContacts()
                .firstOrNull { it.id == id }

            _state.update {
                it.copy(
                    id = id,
                    name = contact?.name.orEmpty(),
                    linePrimary = contact?.subtitle.orEmpty(),
                    lineSecondary = "",
                    avatarPath = contact?.avatarUrl,
                    isInMyContacts = false,
                    isLoading = false,
                )
            }
        }
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch { _effect.send(effect) }
}
