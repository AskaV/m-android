package com.spp.android.myapplication.presentation.designsystem.components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.AccentLight
import com.spp.android.myapplication.presentation.designsystem.theme.TextPrimaryLight
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {},
    containerColor: Color = AccentLight,
    contentColor: Color = TextPrimaryLight
) {
    val height = dimensionResource(id = R.dimen.button_height)
    val desiredRadius = dimensionResource(id = R.dimen.button_corner_radius)
    val safeRadius = if (desiredRadius > height / 2) height / 2 else desiredRadius

    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(height),
        shape = RoundedCornerShape(safeRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor, contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensionResource(id = R.dimen.spacer_extra_small)
        )
    ) {
        Text(text = text.uppercase(), style = MaterialTheme.typography.titleMedium)
    }
}

@PreviewPhones
@Composable
fun FilledButtonPreview() =
    PreviewColumn { FilledButton(text = AppText.Preview.FILLED_BTN_TEXT.text()) }