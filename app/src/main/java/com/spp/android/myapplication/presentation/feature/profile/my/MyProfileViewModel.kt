package com.spp.android.myapplication.presentation.feature.profile.my

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class MyProfileViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _state = MutableStateFlow(MyProfileContract.State())
    val state: StateFlow<MyProfileContract.State> = _state.asStateFlow()

    private val _effect = Channel<MyProfileContract.Effect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(MyProfileContract.Event.Load)
    }

    fun onEvent(event: MyProfileContract.Event) {
        when (event) {
            MyProfileContract.Event.Load,
            MyProfileContract.Event.Refresh -> loadProfile()

            MyProfileContract.Event.EditProfileClicked ->
                emitEffect(MyProfileContract.Effect.NavigateToEditProfile)

            MyProfileContract.Event.ViewContactsClicked ->
                emitEffect(MyProfileContract.Effect.NavigateToContacts)

            MyProfileContract.Event.LogoutClicked ->
                performLogout()

            MyProfileContract.Event.ErrorShown ->
                _state.update { it.copy(error = null) }

            is MyProfileContract.Event.ProfileSaved -> {
                _state.update {
                    it.copy(
                        name = event.username,
                        linePrimary = event.career,
                        lineSecondary = event.address,
                        isCompleted = true
                    )
                }
            }

            MyProfileContract.Event.SignUpFinished -> {
                _state.update { it.copy(isCompleted = false) }
            }

            MyProfileContract.Event.MarkCompleted -> {
                _state.update { it.copy(isCompleted = true) }
            }
        }
    }

    private fun loadProfile() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }

        runCatching {
            StubProfile(
                name = "Lucile Alvarado",
                linePrimary = "Product Designer",
                lineSecondary = "New York, USA",
                isCompleted = false
            )
        }.onSuccess { p ->
            _state.update {
                it.copy(
                    name = p.name,
                    linePrimary = p.linePrimary,
                    lineSecondary = p.lineSecondary,
                    isCompleted = p.isCompleted,
                    isLoading = false
                )
            }
        }.onFailure { throwable ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = throwable.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext))
            }
            emitEffect(MyProfileContract.Effect.ShowMessage(AppText.OtherInfo.FAILED_TO_LOAD_PROFILE.text(appContext)))
        }
    }

    private fun performLogout() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true) }

        runCatching {
            true
        }.onSuccess {
            _state.update { it.copy(isLoading = false) }
            emitEffect(MyProfileContract.Effect.NavigateToAuth)
        }.onFailure { throwable ->
            _state.update {
                it.copy(
                    isLoading = false,
                    error = throwable.message ?: AppText.OtherInfo.UNKNOWN_ERROR.text(appContext))

            }
            emitEffect(MyProfileContract.Effect.ShowMessage(AppText.OtherInfo.LOGOUT_FAILED.text(appContext)))
        }
    }

    private fun emitEffect(effect: MyProfileContract.Effect) = viewModelScope.launch {
        _effect.send(effect)
    }

    private data class StubProfile(
        val name: String,
        val linePrimary: String,
        val lineSecondary: String,
        val isCompleted: Boolean
    )
}