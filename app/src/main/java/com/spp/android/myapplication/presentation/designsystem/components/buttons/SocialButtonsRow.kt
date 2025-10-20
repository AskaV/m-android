package com.spp.android.myapplication.presentation.designsystem.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.components.buttons.parts.SocialButton
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun SocialButtonsRow(
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.social_button_spacing)),
        modifier = modifier
    ) {
        SocialButton(
            iconRes = R.drawable.social_facebook,
            contentDescription = stringResource(R.string.facebook)
        )
        SocialButton(
            iconRes = R.drawable.social_instagram,
            contentDescription = stringResource(R.string.instagram)
        )
        SocialButton(
            iconRes = R.drawable.social_telegram,
            contentDescription = stringResource(R.string.telegram)
        )
    }
}


@PreviewPhones
@Composable
private fun SocialButtonsRowPreview() {
    PreviewColumn {
        SocialButtonsRow()
    }
}