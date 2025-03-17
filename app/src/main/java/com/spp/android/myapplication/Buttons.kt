package com.spp.android.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.ui.theme.*

@Composable
fun SocialButton(iconRes: Int, contentDescription: String) {

    Box(
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.social_button_size))
            .background(White, shape = CircleShape)
            .border(
                dimensionResource(id = R.dimen.button_border_width),
                Orange,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = Orange,
            modifier = Modifier.size(dimensionResource(id = R.dimen.social_icon_size))
        )
    }
}

@Composable
fun CustomOrangeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Orange,
            contentColor = White
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = dimensionResource(id = R.dimen.button_elevation))
    ) {
        Text(text = text, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun EditProfileButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.button_height)),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)),
        border = BorderStroke(dimensionResource(id = R.dimen.button_border_width), GrayText),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = GrayText
        )
    ) {
        Text(
            text = stringResource(R.string.edit_profile),
            style = MaterialTheme.typography.titleMedium
        )
    }
}