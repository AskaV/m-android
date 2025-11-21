package com.spp.android.myapplication.presentation.feature.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun ActionFab(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    contentDescription: String = "",
    onClick: () -> Unit = {},
    alignment: Alignment = Alignment.BottomEnd,
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)

    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape,
        modifier = modifier.padding(pad),
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
        )
    }
}

@PreviewPhones
@Composable
private fun ActionFabPreview() {
    MaterialTheme {
        ActionFab(
            iconRes = R.drawable.recycle_bin,
        )
    }
}
