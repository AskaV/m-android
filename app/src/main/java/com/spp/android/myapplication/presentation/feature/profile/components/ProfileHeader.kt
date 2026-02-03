package com.spp.android.myapplication.presentation.feature.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    name: String = "",
    linePrimary: String = "",
    lineSecondary: String = "",
    avatarRes: Int = R.drawable.baseline_account_circle_avatar,
    avatarPath: String? = "",
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val avatarSize = dimensionResource(id = R.dimen.avatar_size)

        if (avatarPath.isNullOrBlank()) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = null,
                modifier = Modifier.size(avatarSize).clip(CircleShape),
            )
        } else {
            AsyncImage(
                model = avatarPath,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.baseline_account_circle_avatar),
                error = painterResource(R.drawable.baseline_account_circle_avatar),
                modifier = Modifier.size(avatarSize).clip(CircleShape),
            )
        }

        Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_large)))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_small)))

        Text(
            text = linePrimary,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))

        Text(
            text = lineSecondary,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@PreviewPhones
@Composable
private fun ProfileHeaderPreview() {
    PreviewColumn {
        Surface(color = MaterialTheme.colorScheme.background) {
            ProfileHeader(
                name = AppText.MyProfile.NAME.text(),
                linePrimary = AppText.MyProfile.CAREER.text(),
                lineSecondary = AppText.MyProfile.ADDRESS.text(),
            )
        }
    }
}
