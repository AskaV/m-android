package com.spp.android.myapplication.presentation.feature.components.imageload

import android.net.Uri
import androidx.compose.runtime.Immutable

object GalleryPickerContract {

    @Immutable
    data class State(
        val isVisible: Boolean = false,
        val allowDeleteCurrent: Boolean = false,
        val isLoading: Boolean = false,
        val error: String = "",
    )

    sealed interface Event {
        data object Show : Event
        data object Dismiss : Event

        data object OpenGallery : Event
        data object OpenCamera : Event
        data object DeleteCurrent : Event

        data class PhotoPicked(val uri: Uri) : Event
        data object ErrorShown : Event
        data object Clear : Event
    }

    sealed interface Effect {
        data object LaunchGalleryPicker : Effect
        data object LaunchCamera : Effect
        data class ShowMessage(val message: String) : Effect
        data class ReturnResult(val uri: Uri?) : Effect
    }
}