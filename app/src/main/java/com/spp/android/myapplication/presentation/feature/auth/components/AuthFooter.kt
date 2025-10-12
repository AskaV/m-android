package com.spp.android.myapplication.presentation.feature.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun AuthFooter(
    modifier: Modifier = Modifier,
    question: String,
    actionText: String,
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.weight(1f))
        Text(
            text = question,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.width(dimensionResource(R.dimen.spacer_additional)))

        Text(
            text = actionText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.clickable { onActionClick() },
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.weight(1f))
    }
}

@PreviewPhones
@Composable
private fun AuthFooterPreview() {
    PreviewColumn {
        AuthFooter(
            question = AppText.Login.DONT_HAVE_ACCOUNT.text(),
            actionText = AppText.Login.SIGN_UP.text(),
        )
    }
}