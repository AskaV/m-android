package com.spp.android.myapplication.presentation.feature.components.imageload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GalleryPickerScreen(
    modifier: Modifier = Modifier,
    onResult: (Uri?) -> Unit,
    startVisible: Boolean = false,
    vm: GalleryPickerViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(GetContent()) { uri ->
        uri?.let { vm.onEvent(GalleryPickerContract.Event.PhotoPicked(it)) }
    }

    LaunchedEffect(startVisible) {
        if (startVisible) vm.onEvent(GalleryPickerContract.Event.Show)
        vm.effect.collectLatest { eff ->
            when (eff) {
                GalleryPickerContract.Effect.LaunchGalleryPicker ->
                    galleryLauncher.launch("image/*")

                GalleryPickerContract.Effect.LaunchCamera -> { /* TODO: */ }

                is GalleryPickerContract.Effect.ShowMessage -> {/* TODO: */ }

                is GalleryPickerContract.Effect.ReturnResult -> onResult(eff.uri)
            }
        }
    }

    GalleryPickerScreenContent(
        state = state,
        onDismiss = { vm.onEvent(GalleryPickerContract.Event.Dismiss) },
        onOpenGallery = { vm.onEvent(GalleryPickerContract.Event.OpenGallery) },
        onOpenCamera = { vm.onEvent(GalleryPickerContract.Event.OpenCamera) },
        onDeleteCurrent = { vm.onEvent(GalleryPickerContract.Event.DeleteCurrent) },
        modifier = modifier
    )
}