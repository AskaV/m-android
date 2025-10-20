package com.spp.android.myapplication.presentation.designsystem.imageload

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun AvatarPicker(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    avatarSize: Dp = dimensionResource(id = R.dimen.avatar_size),
    badgeSize: Dp = dimensionResource(id = R.dimen.badge_size),
    space: Dp = dimensionResource(R.dimen.spacer_medium),
    badgeBg: Color = MaterialTheme.colorScheme.onSurface,
    badgeIconTint: Color = Color.White,
    showBadge: Boolean = false
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Icon(
            painter = painterResource(R.drawable.baseline_account_circle_avatar),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(avatarSize)
        )

        if (showBadge) {
            Spacer(modifier = Modifier.width(space))

            Box(
                modifier = Modifier.size(badgeSize).clip(CircleShape).background(badgeBg)
                    .clickable(onClick = onClick), contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    painter = painterResource(R.drawable.ic_camera),
                    contentDescription = null,
                    tint = badgeIconTint,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "+",
                    color = badgeIconTint,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.TopEnd).offset(x = (-4).dp, y = (-1).dp)
                )
            }
        }
    }
}

@PreviewPhones
@Composable
private fun AvatarPickerBadgePreview() {
    PreviewColumn {
        AvatarPicker(
            showBadge = true
        )
    }
}

@PreviewPhones
@Composable
private fun AvatarPickerPreview() {
    PreviewColumn {
        AvatarPicker(
        )
    }
}