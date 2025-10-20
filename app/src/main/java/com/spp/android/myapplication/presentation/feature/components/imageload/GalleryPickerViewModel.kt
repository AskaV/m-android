package com.spp.android.myapplication.presentation.feature.components.imageload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.LaunchCamera
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.LaunchGalleryPicker
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.ReturnResult
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.Clear
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.DeleteCurrent
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.Dismiss
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.ErrorShown
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.OpenCamera
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.OpenGallery
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.PhotoPicked
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.Show
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryPickerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GalleryPickerContract.State())
    val state: StateFlow<GalleryPickerContract.State> = _state.asStateFlow()

    private val _effect = Channel<GalleryPickerContract.Effect>(Channel.BUFFERED)
    val effect: Flow<GalleryPickerContract.Effect> = _effect.receiveAsFlow()

    fun onEvent(event: GalleryPickerContract.Event) {
        when (event) {
            Show -> _state.update { it.copy(isVisible = true) }

            Dismiss -> _state.update { it.copy(isVisible = false) }

            OpenGallery -> viewModelScope.launch {
                _effect.send(
                    LaunchGalleryPicker
                )
            }

            OpenCamera -> viewModelScope.launch {
                _effect.send(
                    LaunchCamera
                )
            }

            DeleteCurrent -> viewModelScope.launch {
                _state.update { it.copy(isVisible = false) }
                _effect.send(ReturnResult(null))
            }

            is PhotoPicked -> viewModelScope.launch {
                _state.update { it.copy(isVisible = false) }
                _effect.send(ReturnResult(event.photoPickedUri))
            }

            ErrorShown -> _state.update { it.copy() }

            Clear -> _state.update { GalleryPickerContract.State() }
        }
    }
}