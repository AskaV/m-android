package com.spp.android.myapplication.presentation.designsystem.components.buttons.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewRow
import com.spp.android.myapplication.presentation.designsystem.theme.socialIcon

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
                color = Color.Transparent,
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

@PreviewPhones
@Composable
fun SocialButtonPreview() = PreviewRow {
    SocialButton(iconRes = R.drawable.social_facebook, contentDescription = "Search")
    SocialButton(iconRes = R.drawable.social_instagram, contentDescription = "Add")
    SocialButton(iconRes = R.drawable.social_telegram, contentDescription = "Call")
}
