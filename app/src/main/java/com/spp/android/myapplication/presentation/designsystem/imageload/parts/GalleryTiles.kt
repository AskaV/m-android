package com.spp.android.myapplication.presentation.designsystem.imageload.parts

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
internal fun CameraTile(
    size: Dp, onClick: () -> Unit = {}
) {
    Box(
        Modifier.size(size).clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.onTertiary).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_camera),
            contentDescription = "Open gallery"
        )
    }
}

@Composable
internal fun PlaceholderTile(size: Dp) {
    Box(
        Modifier.size(size).clip(RoundedCornerShape(10))
            .background(MaterialTheme.colorScheme.onTertiary)
    )
}

@PreviewPhones
@Composable
private fun CameraTilePreview() = PreviewColumn {
    CameraTile(size = dimensionResource(id = R.dimen.social_button_size))
}