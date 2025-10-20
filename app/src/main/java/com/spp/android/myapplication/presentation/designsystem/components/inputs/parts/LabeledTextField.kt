package com.spp.android.myapplication.presentation.designsystem.components.inputs.parts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.texts.t

@Suppress("LongParameterList")
@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    kind: FieldKind,
    placeholder: String = "",
    error: String = "",
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {},
    spacerAfter: Boolean = true,
    labelTextColor: Color = MaterialTheme.colorScheme.onSurface,
    valueTextColor: Color = MaterialTheme.colorScheme.onBackground,
    trailingIcon: @Composable () -> Unit = {},
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground
    )
) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = labelTextColor,
            modifier = Modifier.fillMaxWidth()
        )

        AppTextField(
            value = value,
            onValueChange = onValueChange,
            kind = kind,
            placeholder = placeholder,
            isError = error.isNotEmpty(),
            imeAction = imeAction,
            onImeAction = onImeAction,
            trailingIcon = trailingIcon,
            textStyle = textStyle,
            valueTextColor = valueTextColor
        )

        Box(
            modifier = Modifier.padding(
                start = dimensionResource(R.dimen.spacer_small),
                top = dimensionResource(R.dimen.spacer_small)
            ).heightIn(min = dimensionResource(R.dimen.form_supporting_text_min_height))
                .fillMaxWidth()
        ) {
            if (error.isNotEmpty()) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (spacerAfter) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.button_corner_radius)))
        }
    }
}

@PreviewPhones
@Composable
fun LabeledTextFieldEmailPreview() = PreviewColumn {
    var v by remember { mutableStateOf(AppText.Preview.EMAIL) }
    LabeledTextField(
        label = AppText.Login.EMAIL_LABEL.text(),
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Email,
        placeholder = AppText.Preview.EMAIL,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@PreviewPhones
@Composable
fun LabeledTextFieldPasswordErrorPreview() = PreviewColumn {
    var v by remember { mutableStateOf(AppText.Preview.PASSWORD) }
    LabeledTextField(
        label = AppText.Login.PASSWORD_LABEL.text(),
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Password,
        placeholder = AppText.Preview.DOTS,
        error = AppText.Login.PASSWORD_ERROR_TEMPLATE.t(AppText.Integers.PASSWORD_MIN_LENGTH),
        imeAction = ImeAction.Done,
        spacerAfter = false,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}