package com.spp.android.myapplication.presentation.feature.auth.login

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.texts.TextKey
import com.spp.android.myapplication.presentation.texts.TextKeyWithArgs

object LoginContract {
    @Immutable
    data class State(
        val email: String = "",
        val password: String = "",
        val rememberMe: Boolean = false,
        val emailErrorKey: TextKey? = null,
        val passwordErrorKey: TextKeyWithArgs? = null,
        val errorKey: TextKey? = null,
        val emailTouched: Boolean = false,
        val passwordTouched: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = "",
    )

    sealed interface Event {
        data class EmailChanged(
            val email: String,
        ) : Event

        data class PasswordChanged(
            val password: String,
        ) : Event

        data class RememberChanged(
            val isChecked: Boolean,
        ) : Event

        data object EmailBlur : Event

        data object PasswordBlur : Event

        data object ForgotPasswordClicked : Event

        data object Submit : Event

        data object ErrorShown : Event

        data object Clear : Event
    }

    sealed interface Effect {
        data object NavigateToHome : Effect

        data object ForgotPassword : Effect

        data class ShowToast(
            val text: String,
        ) : Effect
    }
}
