package com.spp.android.myapplication.presentation.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

enum class OutlinedButtonStyle { Primary, Secondary, OnBackground }

@Composable
fun OutlinedBorderButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: OutlinedButtonStyle = OutlinedButtonStyle.Secondary
) {
    val (borderColor, contentColor, textStyle, transformedText) = when (style) {
        OutlinedButtonStyle.Primary -> arrayOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.typography.titleMedium,
            text.uppercase()
        )

        OutlinedButtonStyle.Secondary -> arrayOf(
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.colorScheme.onSecondary,
            MaterialTheme.typography.titleMedium,
            text
        )

        OutlinedButtonStyle.OnBackground -> arrayOf(
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.colorScheme.onBackground,
            MaterialTheme.typography.titleMedium,
            text
        )
    }

    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
        border = BorderStroke(
            dimensionResource(id = R.dimen.button_border_width),
            borderColor as Color
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor as Color
        )
    ) {
        Text(
            text = transformedText as String,
            style = textStyle as androidx.compose.ui.text.TextStyle
        )
    }
}

@PreviewPhones
@Composable
fun OutlinedBorderButtonPrimaryPreview() =
    PreviewColumn {
        OutlinedBorderButton(
            text = "Outlined Button Text",
            onClick = {},
            style = OutlinedButtonStyle.Primary
        )
    }

@PreviewPhones
@Composable
fun OutlinedBorderButtonSecondaryPreview() =
    PreviewColumn(background = { MaterialTheme.colorScheme.surface }) {
        OutlinedBorderButton(
            text = "Outlined Button Text",
            onClick = {},
            style = OutlinedButtonStyle.Secondary
        )
    }