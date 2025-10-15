package com.spp.android.myapplication.presentation.feature.contacts.list

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.feature.contacts.components.demoUsers
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Effect
import com.spp.android.myapplication.presentation.feature.contacts.list.ContactsContract.Event.*
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
class ContactsViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context, private val repository: ContactsRepository

) : ViewModel() {

    private val _state = MutableStateFlow(ContactsContract.State())
    val state: StateFlow<ContactsContract.State> = _state.asStateFlow()

    private val _effect = Channel<Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(Load)
    }

    fun onEvent(event: ContactsContract.Event) {
        when (event) {
            is Load -> load()
            is BackClicked -> sendEffect(Effect.NavigateBack)
            is SearchClicked -> sendEffect(Effect.OpenSearch)
            is AddContactsClicked -> sendEffect(Effect.OpenAddContacts)
            is ContactClicked -> sendEffect(
                Effect.OpenContactProfile(
                    event.item.id
                )
            )

            is DeleteClicked -> delete(event.item)
            is ErrorShown -> _state.update { it.copy(error = null) }

            is ContactLongClicked -> {
                _state.update { st ->
                    st.copy(
                        selected = setOf(event.item.id), isSelectionMode = true
                    )
                }
            }

            is ContactSelectionToggled -> {
                _state.update { st ->
                    val newSelected = st.selected.toMutableSet().apply {
                        if (contains(event.item.id)) remove(event.item.id) else add(event.item.id)
                    }
                    st.copy(
                        selected = newSelected, isSelectionMode = newSelected.isNotEmpty()
                    )
                }
            }

            is BulkDeleteClicked -> {
                val ids = _state.value.selected
                _state.update { st ->
                    st.copy(
                        items = st.items.filterNot { ids.contains(it.id) },
                        selected = emptySet(),
                        isSelectionMode = false
                    )
                }
                sendEffect(
                    Effect.ShowMessage(
                        AppText.OtherInfo.CONTACTS_REMOVED.text(appContext)
                    )
                )
            }

            is ExitSelectionMode -> {
                _state.update { it.copy(selected = emptySet(), isSelectionMode = false) }
            }
        }
    }

    private fun load() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        runCatching {
            if (ContextCompat.checkSelfPermission(
                    appContext, android.Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                repository.loadContacts()
            } else {
                demoUsers()
            }
        }.onSuccess { list ->
            _state.update { it.copy(items = list, isLoading = false) }
        }.onFailure { t ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = t.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext)
                )
            }
            sendEffect(
                Effect.ShowMessage(
                    AppText.OtherInfo.CONTACTS_LOAD_FAILED.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun delete(item: ContactUi) = viewModelScope.launch {
        _state.update { it.copy(items = it.items.filterNot { c -> c.id == item.id }) }
        sendEffect(Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED.text(appContext)))
    }

    private fun sendEffect(effect: Effect) = viewModelScope.launch {
        _effect.send(effect)
    }
}