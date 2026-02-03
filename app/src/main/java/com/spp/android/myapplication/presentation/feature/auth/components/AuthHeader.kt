package com.spp.android.myapplication.presentation.feature.auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun AuthHeader(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String = "",
) {
    Column(
        modifier = modifier.fillMaxWidth().wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacer_small)))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@PreviewPhones
@Composable
private fun AuthHeaderPreview() {
    PreviewColumn {
        AuthHeader(
            title = AppText.Login.TITLE.text(),
            subtitle = AppText.Login.SUBTITLE.text(),
        )
    }
}
