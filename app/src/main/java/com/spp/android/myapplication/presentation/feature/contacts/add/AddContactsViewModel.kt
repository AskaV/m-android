package com.spp.android.myapplication.presentation.feature.contacts.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.contacts.components.demoUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    @ApplicationContext private val app: Context
) : ViewModel() {

    private val _state = MutableStateFlow(AddContactsContract.State())
    val state = _state.asStateFlow()

    private val _effect = Channel<AddContactsContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(AddContactsContract.Event.Load)
    }

    fun onEvent(e: AddContactsContract.Event) {
        when (e) {
            AddContactsContract.Event.Load -> load()
            AddContactsContract.Event.BackClicked -> emit(AddContactsContract.Effect.NavigateBack)

            AddContactsContract.Event.SearchClicked -> emit(AddContactsContract.Effect.OpenSearch)

            is AddContactsContract.Event.ToggleSelect -> {
                _state.update { st ->
                    val ns = st.selected.toMutableSet().apply {
                        if (contains(e.item.id)) remove(e.item.id) else add(e.item.id)
                    }
                    st.copy(selected = ns)
                }
            }

            AddContactsContract.Event.MassAddClicked -> {
                val count = _state.value.selected.size
                if (count > 0) {
                    emit(
                        AddContactsContract.Effect.ShowMessage(
                            "Added $count contact(s)"
                        )
                    )
                    _state.update { it.copy(selected = emptySet()) }
                }
            }

            AddContactsContract.Event.ErrorShown -> _state.update { it.copy() }

            is AddContactsContract.Event.AddClicked -> {
                _state.update { st ->
                    st.copy(items = st.items.filterNot { it.id == e.item.id })
                }
                emit(AddContactsContract.Effect.ShowMessage("Added ${e.item.name}"))
            }
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true) }
        _state.update { it.copy(items = demoUsers(), isLoading = false) }
    }

    private fun emit(e: AddContactsContract.Effect) = viewModelScope.launch { _effect.send(e) }
}