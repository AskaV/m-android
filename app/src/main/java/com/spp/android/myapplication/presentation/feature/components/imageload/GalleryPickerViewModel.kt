package com.spp.android.myapplication.presentation.feature.components.imageload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            GalleryPickerContract.Event.Show ->
                _state.update { it.copy(isVisible = true) }

            GalleryPickerContract.Event.Dismiss ->
                _state.update { it.copy(isVisible = false) }

            GalleryPickerContract.Event.OpenGallery ->
                viewModelScope.launch { _effect.send(GalleryPickerContract.Effect.LaunchGalleryPicker) }

            GalleryPickerContract.Event.OpenCamera ->
                viewModelScope.launch { _effect.send(GalleryPickerContract.Effect.LaunchCamera) }

            GalleryPickerContract.Event.DeleteCurrent ->
                viewModelScope.launch {
                    _state.update { it.copy(isVisible = false) }
                    _effect.send(GalleryPickerContract.Effect.ReturnResult(null))
                }

            is GalleryPickerContract.Event.PhotoPicked ->
                viewModelScope.launch {
                    _state.update { it.copy(isVisible = false) }
                    _effect.send(GalleryPickerContract.Effect.ReturnResult(event.uri))
                }

            GalleryPickerContract.Event.ErrorShown ->
                _state.update { it.copy() }

            GalleryPickerContract.Event.Clear ->
                _state.value = GalleryPickerContract.State()
        }
    }
}