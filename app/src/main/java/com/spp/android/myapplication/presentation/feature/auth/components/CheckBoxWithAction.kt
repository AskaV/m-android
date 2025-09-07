package com.spp.android.myapplication.presentation.feature.auth.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.AuthPreviewText
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun CheckBoxWithAction(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    label: String,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(2.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Transparent,
                        uncheckedColor = Color.Transparent,
                        checkmarkColor = MaterialTheme.colorScheme.onBackground,
                        disabledCheckedColor = Color.Transparent,
                        disabledUncheckedColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(0.75f)
                )
            }

            Spacer(Modifier.width(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (!actionText.isNullOrEmpty()) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                softWrap = false,
                modifier = if (onActionClick != null)
                    Modifier.clickable { onActionClick() }
                else Modifier
            )
        }
    }
}

@PreviewPhones
@Composable
private fun CheckBoxWithActionPreviewFigma() {
    var checked by remember { mutableStateOf(true) }
    PreviewColumn {
        CheckBoxWithAction(
            checked = checked,
            onCheckedChange = { checked = it },
            label = AuthPreviewText.Login.REMEMBER_ME,
            actionText = AuthPreviewText.Login.FORGOT_PASSWORD
        )
        CheckBoxWithAction(
            checked = checked,
            onCheckedChange = { checked = it },
            label = AuthPreviewText.Register.REMEMBER_ME
        )
    }
}