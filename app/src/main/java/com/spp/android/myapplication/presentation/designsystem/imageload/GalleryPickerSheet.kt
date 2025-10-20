package com.spp.android.myapplication.presentation.designsystem.imageload

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.imageload.parts.GalleryActions
import com.spp.android.myapplication.presentation.designsystem.imageload.parts.GalleryTilesRow
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun GalleryPickerSheet(
    modifier: Modifier = Modifier,
    showCamera: Boolean = true,
    onCameraClick: () -> Unit = {},
    onOpenGallery: () -> Unit = {},
    onDeleteCurrent: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    val corner = dimensionResource(id = R.dimen.spacer_small)
    val spacing = dimensionResource(id = R.dimen.spacer_small)
    val actionsSidePadding = dimensionResource(id = R.dimen.spacer_medium)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(corner),
        color = MaterialTheme.colorScheme.tertiary
    ) {
        Column(
            Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GalleryTilesRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = spacing, top = spacing, end = spacing),
                showCamera = showCamera,
                onCameraClick = onCameraClick,
                spacing = spacing
            )

            Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_large)))

            GalleryActions(
                modifier = Modifier.padding(horizontal = actionsSidePadding),
                onOpenGallery = onOpenGallery,
                onCancel = onCancel
            )

            Spacer(Modifier.height(spacing))
        }
    }
}


@PreviewPhones
@Composable
fun GalleryPickerAddPhotoPreview() = PreviewColumn {
    GalleryPickerSheet()
}