package com.spp.android.myapplication.presentation.ui.screens.commoncomponents.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.ui.preview.PreviewColumn
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.screens.authorization.AuthPreviewText

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    text: String = AuthPreviewText.Register.G_BUTTON,
    onClick: () -> Unit,
    enabled: Boolean = true,
    borderWidth: Dp = 1.dp
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner_radius)),
        border = BorderStroke(borderWidth, Color(0xFFE0E0E0)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp),
        contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.spacer_medium))
    ) {
        val gap = dimensionResource(R.dimen.spacer_medium)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_google_logo),
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(gap))
            Text(text.uppercase(), style = MaterialTheme.typography.titleLarge)
        }
    }
}

@PreviewPhones
@Composable
private fun GoogleButtonPreview() = PreviewColumn {
    GoogleButton(text = AuthPreviewText.Register.G_BUTTON, onClick = {})
}