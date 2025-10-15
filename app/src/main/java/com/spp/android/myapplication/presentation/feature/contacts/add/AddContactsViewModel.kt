package com.spp.android.myapplication.presentation.feature.contacts.add

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.contacts.add.AddContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.add.AddContactsContract.Event.*
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
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
                        if (contains(e.item.id)) remove(e.item.id) else add(e.item.id)
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
                    st.copy(items = st.items.filterNot { it.id == e.item.id })
                }
                sendEffect(Effect.ShowMessage("Added ${e.item.name}"))
            }
        }
    }

    private fun load() {
        _state.update { it.copy(isLoading = true) }
        _state.update { it.copy(items = demoUsers(), isLoading = false) }
    }

    private fun sendEffect(e: Effect) = viewModelScope.launch { _effect.send(e) }
}