package com.spp.android.myapplication.presentation.feature.auth.signup

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.designsystem.components.inputs.AuthFieldsState
import com.spp.android.myapplication.presentation.texts.TextKey
import com.spp.android.myapplication.presentation.texts.TextKeyWithArgs

object SignUpContract {

    // Base
    @Immutable
    data class State(
        val fields: AuthFieldsState = AuthFieldsState(),
        val rememberMe: Boolean = false,
        val isLoading: Boolean = false,
        val errorKey: TextKey? = null
    )

    // Extended profile
    @Immutable
    data class ProfileState(
        val username: String = "",
        val phone: String = "",
        val usernameErrorKey: TextKeyWithArgs? = null,
        val phoneErrorKey: TextKeyWithArgs? = null,
        val avatar: Uri? = null,
        val isLoading: Boolean = false,
        val errorKey: TextKey? = null
    )

    sealed interface Event {
        data class EmailChanged(val email: String) : Event
        data class PasswordChanged(val password: String) : Event
        data class RememberChanged(val isChecked: Boolean) : Event
        data object SubmitRegister : Event
        data object RegisterWithGoogle : Event
        data object ErrorShown : Event

        data object NavigateToExtendedRequested : Event

        data class UsernameChanged(val username: String) : Event
        data class PhoneChanged(val phone: String) : Event
        data class AvatarPicked(val avatarUri: Uri) : Event
        data object PickAvatar : Event
        data object CancelExtended : Event
        data object ForwardExtended : Event

        data object EmailBlur : Event
        data object PasswordBlur : Event
        data object UsernameBlur : Event
        data object PhoneBlur : Event
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