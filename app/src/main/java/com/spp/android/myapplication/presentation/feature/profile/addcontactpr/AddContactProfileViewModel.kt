package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddContactProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddContactProfileContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<AddContactProfileContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(e: AddContactProfileContract.Event) {
        when (e) {
            is AddContactProfileContract.Event.Load -> load(e.id)
            AddContactProfileContract.Event.BackClicked -> emit(AddContactProfileContract.Effect.NavigateBack)

            AddContactProfileContract.Event.MessageClicked -> emit(
                AddContactProfileContract.Effect.ShowMessage(
                    "Open chat"
                )
            )

            AddContactProfileContract.Event.AddToContactsClicked -> {
                emit(AddContactProfileContract.Effect.ShowMessage("Added to contacts"))
                _state.update { it.copy(isInMyContacts = true) }
            }

            AddContactProfileContract.Event.ErrorShown -> _state.update { it.copy() }
        }
    }

    private fun load(id: String) {
        _state.update { it.copy(isLoading = true) }
        _state.update {
            it.copy(
                id = id,
                name = "Jenny Walker",
                linePrimary = "Make-up artist",
                lineSecondary = "775 Westminster Ave APT D5\nBrooklyn, NY, 11230",
                isInMyContacts = false,
                isLoading = false
            )
        }
    }

    private fun emit(e: AddContactProfileContract.Effect) =
        viewModelScope.launch { _effect.send(e) }
}