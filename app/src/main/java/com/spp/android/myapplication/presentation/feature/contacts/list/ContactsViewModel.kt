package com.spp.android.myapplication.presentation.feature.contacts.list

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.data.contacts.ContactsRepository
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.ContactPreviewText
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

    private val _effect = Channel<ContactsContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(ContactsContract.Event.Load)
    }

    fun onEvent(event: ContactsContract.Event) {
        when (event) {
            is ContactsContract.Event.Load -> load()
            is ContactsContract.Event.BackClicked -> emit(ContactsContract.Effect.NavigateBack)
            is ContactsContract.Event.SearchClicked -> emit(ContactsContract.Effect.OpenSearch)
            is ContactsContract.Event.AddContactsClicked -> emit(ContactsContract.Effect.OpenAddContacts)
            is ContactsContract.Event.ContactClicked -> emit(
                ContactsContract.Effect.OpenContactProfile(
                    event.item.id
                )
            )

            is ContactsContract.Event.DeleteClicked -> delete(event.item)
            is ContactsContract.Event.ErrorShown -> _state.update { it.copy(error = null) }

            is ContactsContract.Event.ContactLongClicked -> {
                _state.update { st ->
                    st.copy(
                        selected = setOf(event.item.id), isSelectionMode = true
                    )
                }
            }

            is ContactsContract.Event.ContactSelectionToggled -> {
                _state.update { st ->
                    val newSelected = st.selected.toMutableSet().apply {
                        if (contains(event.item.id)) remove(event.item.id) else add(event.item.id)
                    }
                    st.copy(
                        selected = newSelected, isSelectionMode = newSelected.isNotEmpty()
                    )
                }
            }

            is ContactsContract.Event.BulkDeleteClicked -> {
                val ids = _state.value.selected
                _state.update { st ->
                    st.copy(
                        items = st.items.filterNot { ids.contains(it.id) },
                        selected = emptySet(),
                        isSelectionMode = false
                    )
                }
                emit(
                    ContactsContract.Effect.ShowMessage(
                        AppText.OtherInfo.CONTACTS_REMOVED.text(appContext)
                    )
                )
            }

            is ContactsContract.Event.ExitSelectionMode -> {
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
                demoContacts()
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
            emit(
                ContactsContract.Effect.ShowMessage(
                    AppText.OtherInfo.CONTACTS_LOAD_FAILED.text(
                        appContext
                    )
                )
            )
        }
    }

    private fun delete(item: ContactUi) = viewModelScope.launch {
        _state.update { it.copy(items = it.items.filterNot { c -> c.id == item.id }) }
        emit(ContactsContract.Effect.ShowMessage(AppText.OtherInfo.CONTACTS_REMOVED.text(appContext)))
    }

    private fun emit(effect: ContactsContract.Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private fun demoContacts(): List<ContactUi> = listOf(
        ContactUi("1", ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1),
        ContactUi("2", ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2),
        ContactUi("3", ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3),
        ContactUi("4", ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4),
        ContactUi("5", ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5),
        ContactUi("6", ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6),


        ContactUi("7", ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1),
        ContactUi("8", ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2),
        ContactUi("9", ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3),
        ContactUi("10", ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4),
        ContactUi("11", ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5),
        ContactUi("12", ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6),
    )
}