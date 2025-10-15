package com.spp.android.myapplication.presentation.feature.profile.addcontactpr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.addcontactpr.AddContactProfileContract.Event.Load
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddContactProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(AddContactProfileContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    fun onEvent(e: AddContactProfileContract.Event) {
        when (e) {
            is Load -> load(e.id)
            is AddContactProfileContract.Event.BackClicked -> sendEffect(Effect.NavigateBack)

            is AddContactProfileContract.Event.MessageClicked -> sendEffect(
                Effect.ShowMessage(
                    "Open chat"
                )
            )

            is AddContactProfileContract.Event.AddToContactsClicked -> {
                sendEffect(Effect.ShowMessage("Added to contacts"))
                _state.update { it.copy(isInMyContacts = true) }
            }

            is AddContactProfileContract.Event.ErrorShown -> _state.update { it.copy() }
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

    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}