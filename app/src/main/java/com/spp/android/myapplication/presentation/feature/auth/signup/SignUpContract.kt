package com.spp.android.myapplication.presentation.feature.auth.signup

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFieldsState

object SignUpContract {

    // Base
    @Immutable
    data class State(
        val fields: AuthFieldsState = AuthFieldsState(),
        val rememberMe: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null
    )

    // Extended profile
    @Immutable
    data class ProfileState(
        val username: String = "",
        val phone: String = "",
        val usernameError: String? = null,
        val phoneError: String? = null,
        val usernameTouched: Boolean = false,
        val phoneTouched: Boolean = false,

        val isLoading: Boolean = false,
        val error: String? = null
    )

    sealed interface Event {
        data class EmailChanged(val value: String) : Event
        data class PasswordChanged(val value: String) : Event
        data class RememberChanged(val value: Boolean) : Event
        data object SubmitRegister : Event
        data object RegisterWithGoogle : Event
        data object ErrorShown : Event

        data object NavigateToExtendedRequested : Event

        //Extended
        data class UsernameChanged(val value: String) : Event
        data class PhoneChanged(val value: String) : Event
        data object PickAvatar : Event
        data object CancelExtended : Event
        data object ForwardExtended : Event
    }

    sealed interface Effect {
        data class ShowMessage(val message: String) : Effect
        data object OpenGoogleSignIn : Effect
        data object NavigateToLogin : Effect
        data object NavigateToExtended : Effect
        data object NavigateToHome : Effect
        data object OpenAvatarPicker : Effect
        data object BackFromExtended : Effect
    }
}