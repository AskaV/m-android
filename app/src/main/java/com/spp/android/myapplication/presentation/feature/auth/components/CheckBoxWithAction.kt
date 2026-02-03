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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun CheckBoxWithAction(
    modifier: Modifier = Modifier,
    checked: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    label: String = "",
    actionText: String = "",
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier.size(dimensionResource(R.dimen.spacer_medium)).border(
                        width = dimensionResource(R.dimen.button_border_width),
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(dimensionResource(R.dimen.button_border_width)),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = Color.Transparent,
                            uncheckedColor = Color.Transparent,
                            checkmarkColor = MaterialTheme.colorScheme.onBackground,
                            disabledCheckedColor = Color.Transparent,
                            disabledUncheckedColor = Color.Transparent,
                        ),
                    modifier = Modifier.fillMaxSize().scale(0.75f),
                )
            }

            Spacer(Modifier.width(dimensionResource(R.dimen.spacer_small)))

            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        if (actionText.isNotEmpty()) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.clickable { onActionClick() },
            )
        }
    }
}

@PreviewPhones
@Composable
private fun CheckBoxWithActionPreviewFigma() {
    PreviewColumn {
        CheckBoxWithAction(
            label = AppText.Login.REMEMBER_ME.text(),
            actionText = AppText.Login.FORGOT_PASSWORD.text(),
        )
    }
}

@PreviewPhones
@Composable
private fun CheckBoxWithActionPreview2Figma() {
    PreviewColumn {
        CheckBoxWithAction(
            checked = true,
            label = AppText.Login.REMEMBER_ME.text(),
        )
    }
}
