package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.runtime.Immutable

object LoginContract {

    @Immutable
    data class State(
        val email: String = "",
        val password: String = "",
        val rememberMe: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val emailTouched: Boolean = false,
        val passwordTouched: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data class EmailChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data class RememberChanged(val value: Boolean) : Event
        data object EmailBlur : Event
        data object PasswordBlur : Event

        data object Submit : Event
        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateToHome : Effect
        data class ShowMessage(val message: String) : Effect
    }
}