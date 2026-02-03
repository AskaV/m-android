package com.spp.android.myapplication.presentation.designsystem.imageload.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
internal fun GalleryActions(
    modifier: Modifier = Modifier,
    onOpenGallery: () -> Unit = {},
    onDeleteCurrent: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Column(modifier) {
        ActionItem(
            text = AppText.GalleryStrings.OPEN_GALLERY.text(),
            enabled = true,
            onClick = onOpenGallery,
            colorOverride = MaterialTheme.colorScheme.onBackground,
        )

        ActionItem(
            text = AppText.GalleryStrings.DELETE_PHOTO.text(),
            enabled = false,
            dimmed = true,
        )

        val cancelColor = MaterialTheme.colorScheme.onBackground

        ActionItem(
            text = AppText.GalleryStrings.CANCEL.text(),
            enabled = true,
            onClick = onCancel,
            colorOverride = cancelColor,
        )
    }
}

@Composable
fun ActionItem(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit = {},
    dimmed: Boolean = false,
    colorOverride: Color? = null,
) {
    val baseColor =
        when {
            dimmed && enabled -> MaterialTheme.colorScheme.onSurfaceVariant
            !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> MaterialTheme.colorScheme.onSurface
        }
    val color = colorOverride ?: baseColor

    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        textAlign = TextAlign.Center,
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = dimensionResource(id = R.dimen.button_height))
                .padding(vertical = dimensionResource(id = R.dimen.spacer_small))
                .clickable(enabled = enabled) { onClick() },
    )
}

@Preview(showBackground = true)
@Composable
private fun GalleryActionsPreview() {
    MaterialTheme {
        GalleryActions()
    }
}
