package com.spp.android.myapplication.presentation.feature.profile.contact

import androidx.compose.runtime.Immutable
import com.spp.android.myapplication.presentation.texts.TextKey

object ContactProfileContract {
    @Immutable
    data class State(
        val contactId: Int = 0,
        val name: String = "",
        val linePrimary: String = "",
        val lineSecondary: String = "",
        val avatarPath: String? = null,
        val hasSocial: Boolean = true,
        val isLoading: Boolean = false,
        val errorKey: TextKey? = null,
    )

    sealed interface Event {
        data class Load(
            val contactId: Int,
        ) : Event

        data object BackClicked : Event

        data object MessageClicked : Event

        data object AddClicked : Event

        data object ErrorShown : Event
    }

    sealed interface Effect {
        data object NavigateBack : Effect

        data class OpenChat(
            val contactId: Int,
        ) : Effect

        data class ShowMessage(
            val messageKey: TextKey,
        ) : Effect
    }
}
