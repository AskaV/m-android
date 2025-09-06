package com.spp.android.myapplication.presentation.ui.screens.commoncomponents.galleryload.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.presentation.ui.preview.PreviewColumn
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones

@Composable
internal fun GalleryTilesRow(
    modifier: Modifier = Modifier,
    showCamera: Boolean,
    onCameraClick: () -> Unit,
    maxColumns: Int,
    minTile: Dp,
    spacing: Dp
) {
    BoxWithConstraints(modifier) {
        val contentWidth = maxWidth
        val columns = computeColumns(contentWidth, minTile, spacing, maxColumns).coerceAtLeast(1)
        val tile = computeTileSize(contentWidth, columns, spacing)

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showCamera) {
                CameraTile(size = tile, onClick = onCameraClick)
            }
            val free = (columns - if (showCamera) 1 else 0).coerceAtLeast(0)
            repeat(free) { PlaceholderTile(size = tile) }
        }
    }
}

@PreviewPhones
@Composable
private fun GalleryTilesRowWithCameraPreview() = PreviewColumn {
    GalleryTilesRow(
        showCamera = true,
        onCameraClick = {},
        maxColumns = 4,
        minTile = 64.dp,
        spacing = 8.dp
    )
}