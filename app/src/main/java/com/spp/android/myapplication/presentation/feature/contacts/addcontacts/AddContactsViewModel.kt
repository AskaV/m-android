package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.AddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.BackClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.Load
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.MassAddClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.SearchClicked
import com.spp.android.myapplication.presentation.feature.contacts.addcontacts.AddContactsContract.Event.ToggleSelect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AddContactsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Load)
    }

    fun onEvent(e: AddContactsContract.Event) {
        when (e) {
            is Load -> load()
            is BackClicked -> sendEffect(Effect.NavigateBack)

            is SearchClicked -> sendEffect(Effect.OpenSearch)

            is ToggleSelect -> {
                _state.update { st ->
                    val ns = st.selected.toMutableSet().apply {
                        if (contains(e.toggleSelect.id)) remove(e.toggleSelect.id) else add(e.toggleSelect.id)
                    }
                    st.copy(selected = ns)
                }
            }

            is MassAddClicked -> {
                val count = _state.value.selected.size
                if (count > 0) {
                    sendEffect(
                        Effect.ShowMessage(
                            "Added $count contact(s)"
                        )
                    )
                    _state.update { it.copy(selected = emptySet()) }
                }
            }

            is ErrorShown -> _state.update { it.copy() }

            is AddClicked -> {
                _state.update { st ->
                    st.copy(items = st.items.filterNot { it.id == e.dddClicked.id })
                }
                sendEffect(Effect.ShowMessage("Added ${e.dddClicked.name}"))
            }
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true) }
        _state.update { it.copy(items = demoUsers(), isLoading = false) }
    }

    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}