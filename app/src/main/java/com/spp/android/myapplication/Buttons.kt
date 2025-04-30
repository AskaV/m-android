package com.spp.android.myapplication

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.ui.theme.AccentLight
import com.spp.android.myapplication.ui.theme.TextPrimaryLight
import com.spp.android.myapplication.ui.theme.TextTertiaryLight
import com.spp.android.myapplication.ui.theme.socialIcon

@Composable
fun SocialButton(
    iconRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(dimensionResource(id = R.dimen.social_button_size))
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
            .border(
                width = dimensionResource(id = R.dimen.button_border_width),
                color = MaterialTheme.colorScheme.socialIcon,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.socialIcon,
            modifier = Modifier.size(dimensionResource(id = R.dimen.social_icon_size))
        )
    }
}

@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = AccentLight,
    contentColor: Color = TextPrimaryLight
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensionResource(id = R.dimen.button_elevation)
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun OutlinedBorderButton(
    text: String = stringResource(R.string.edit_profile),
    onClick: () -> Unit,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    borderColor: Color = TextTertiaryLight,
    contentColor: Color = TextTertiaryLight
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
        border = BorderStroke(
            dimensionResource(id = R.dimen.button_border_width),
            borderColor
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}