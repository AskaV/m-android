package com.spp.android.myapplication.presentation.feature.profile.edit

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.texts.TextKey

object EditProfileContract {
    @Immutable
    data class State(
        val username: String = "",
        val career: String = "",
        val phone: String = "",
        val address: String = "",
        val birthdate: String = "",
        val isLoading: Boolean = false,
        val isSaving: Boolean = false,
        val errorKey: TextKey? = null,
        val usernameErrorKey: TextKey? = null,
        val phoneErrorKey: TextKey? = null,
    )

    sealed interface Event {
        data object Load : Event

        data class UsernameChanged(
            val usernameChanged: String,
        ) : Event

        data class CareerChanged(
            val careerChanged: String,
        ) : Event

        data class PhoneChanged(
            val phoneChanged: String,
        ) : Event

        data class AddressChanged(
            val addressChanged: String,
        ) : Event

        data class BirthdateChanged(
            val birthdateChanged: String,
        ) : Event

        data object SaveClicked : Event

        data object BackClicked : Event

        data object AvatarClicked : Event

        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect

        data object OpenAvatarPicker : Effect

        data class ShowMessage(
            val messageKey: TextKey,
        ) : Effect

        data object Saved : Effect
    }
}
