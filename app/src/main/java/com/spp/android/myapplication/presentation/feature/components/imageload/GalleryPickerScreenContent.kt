package com.spp.android.myapplication.presentation.feature.components.imageload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.window.Dialog
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.imageload.GalleryPickerSheet
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun GalleryPickerScreenContent(
    modifier: Modifier = Modifier,
    state: GalleryPickerContract.State,
    onDismiss: () -> Unit = {},
    onOpenGallery: () -> Unit = {},
    onOpenCamera: () -> Unit = {},
    onDeleteCurrent: () -> Unit = {},
) {
    if (!state.isVisible) return

    Dialog(onDismissRequest = onDismiss) {
        GalleryPickerSheet(
            showCamera = true,
            onCameraClick = onOpenCamera,
            onOpenGallery = onOpenGallery,
            onDeleteCurrent = onDeleteCurrent,
            onCancel = onDismiss,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.spacer_medium)))
                    .background(MaterialTheme.colorScheme.surface),
        )
    }
}

@PreviewPhones
@Composable
fun GalleryPickerAddPhotoPreview() =
    PreviewColumn {
        GalleryPickerSheet(showCamera = true)
    }
