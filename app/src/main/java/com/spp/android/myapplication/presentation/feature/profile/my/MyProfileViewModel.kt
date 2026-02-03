package com.spp.android.myapplication.presentation.feature.profile.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.domain.storage.LocalStorage
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Effect
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.EditProfileClicked
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.Load
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.LogoutClicked
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.MarkCompleted
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.ProfileSaved
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.Refresh
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.SignUpFinished
import com.spp.android.myapplication.presentation.feature.profile.my.MyProfileContract.Event.ViewContactsClicked
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.utils.parseNameFromEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel
    @Inject
    constructor(
        private val localStorage: LocalStorage,
    ) : ViewModel() {
        private val _state = MutableStateFlow(MyProfileContract.State())
        val state: StateFlow<MyProfileContract.State> = _state.asStateFlow()

        private val _effect = Channel<Effect>(Channel.BUFFERED)
        val effect = _effect.receiveAsFlow()
        private var profileJob: Job? = null

        init {
            onEvent(Load)
        }

        fun onEvent(event: Event) {
            when (event) {
                Load, Refresh -> loadProfile()
                EditProfileClicked -> sendEffect(Effect.NavigateToEditProfile)
                ViewContactsClicked -> sendEffect(Effect.NavigateToContacts)
                LogoutClicked -> performLogout()
                ErrorShown -> _state.update { it.copy(errorKey = null) }

                is ProfileSaved -> {
                    _state.update {
                        it.copy(
                            name = event.username,
                            linePrimary = event.career,
                            lineSecondary = event.address,
                            isCompleted = true,
                        )
                    }
                }

                SignUpFinished -> {
                    _state.update { it.copy(isCompleted = false, linePrimary = "", lineSecondary = "") }
                }

                MarkCompleted -> {
                    _state.update { it.copy(isCompleted = true) }
                }
            }
        }

        private fun loadProfile() {
            if (profileJob != null) return

            profileJob =
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true, errorKey = null) }

                    localStorage.userProfile.collect { profile ->
                        if (profile != null) {
                            _state.update {
                                it.copy(
                                    name = profile.username,
                                    linePrimary = profile.career,
                                    lineSecondary = profile.address,
                                    avatarPath = profile.avatarPath,
                                    isCompleted = profile.isCompleted,
                                    isLoading = false,
                                )
                            }
                        } else {
                            _state.update { it.copy(isLoading = false) }
                        }
                    }
                }
        }

        private fun performLogout() =
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                runCatching { true }
                    .onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        sendEffect(Effect.NavigateToAuth)
                    }.onFailure {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorKey = AppText.OtherInfo.UNKNOWN_ERROR,
                            )
                        }
                        sendEffect(Effect.ShowMessage(AppText.OtherInfo.LOGOUT_FAILED))
                    }
            }

        private fun sendEffect(effect: Effect) =
            viewModelScope.launch {
                _effect.send(effect)
            }

        private data class StubProfile(
            val name: String = "",
            val linePrimary: String = "",
            val lineSecondary: String = "",
            val isCompleted: Boolean = false,
        )

        fun onExternalName(name: String) {
            if (name.isNotBlank()) {
                _state.update { it.copy(name = name) }
            }
        }

        fun onExternalEmail(email: String) {
            _state.update { it.copy(email = email) }
            applyDerivedName()
        }

        private fun applyDerivedName() {
            val s = _state.value
            if (s.name.isBlank() && !s.email.isNullOrBlank()) {
                val (first, last) = parseNameFromEmail(s.email)
                val full = listOf(first, last).filter { it.isNotBlank() }.joinToString(" ")
                if (full.isNotBlank()) _state.update { it.copy(name = full) }
            }
        }
    }
