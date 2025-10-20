package com.spp.android.myapplication.presentation.feature.components.imageload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.LaunchCamera
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.LaunchGalleryPicker
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.ReturnResult
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Effect.ShowMessage
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.DeleteCurrent
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.Dismiss
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.OpenCamera
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.OpenGallery
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.PhotoPicked
import com.spp.android.myapplication.presentation.feature.components.imageload.GalleryPickerContract.Event.Show
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GalleryPickerScreen(
    modifier: Modifier = Modifier,
    onResult: (Uri?) -> Unit = {},
    startVisible: Boolean = false,
    vm: GalleryPickerViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(GetContent()) { uri ->
        uri?.let { vm.onEvent(PhotoPicked(it)) }
    }

    LaunchedEffect(startVisible) {
        if (startVisible) vm.onEvent(Show)
        vm.effect.collectLatest { effect ->
            when (effect) {
                is LaunchGalleryPicker -> galleryLauncher.launch("image/*")

                is LaunchCamera -> { /* TODO: */
                }

                is ShowMessage -> {effect.message }
                is ReturnResult -> onResult(effect.returnResultUri)
            }
        }
    }

    GalleryPickerScreenContent(
        state = state,
        onDismiss = { vm.onEvent(Dismiss) },
        onOpenGallery = { vm.onEvent(OpenGallery) },
        onOpenCamera = { vm.onEvent(OpenCamera) },
        onDeleteCurrent = { vm.onEvent(DeleteCurrent) },
        modifier = modifier
    )
}