package com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.inputs

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.ui.preview.PreviewColumn
import com.spp.android.myapplication.presentation.ui.preview.PreviewPhones
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FieldKind
import com.spp.android.myapplication.presentation.ui.screens.authorization.components.forms.data.FormsPreviewText

@Suppress("LongParameterList")
@Composable
fun LabeledTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    kind: FieldKind,
    placeholder: String? = null,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    spacerAfter: Boolean = true,
    trailingIcon: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground
    )
) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        AppTextField(
            value = value,
            onValueChange = onValueChange,
            kind = kind,
            placeholder = placeholder,
            isError = error != null,
            imeAction = imeAction,
            onImeAction = onImeAction,
            trailingIcon = trailingIcon,
            textStyle = textStyle
        )

        Box(
            modifier = Modifier
                .padding(
                    start = dimensionResource(R.dimen.spacer_small),
                    top = dimensionResource(R.dimen.spacer_small)
                )
                .heightIn(min = dimensionResource(R.dimen.form_supporting_text_min_height))
                .fillMaxWidth()
        ) {
            if (error != null) {
                Text(
                    text = error,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        if (spacerAfter) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.login_margin_top_small)))
        }
    }
}

@PreviewPhones
@Composable
fun LabeledTextFieldEmailPreview() = PreviewColumn {
    var v by remember { mutableStateOf(FormsPreviewText.EMAIL) }
    LabeledTextField(
        label = FormsPreviewText.Label.EMAIL,
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Email,
        placeholder = FormsPreviewText.EMAIL,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@PreviewPhones
@Composable
fun LabeledTextFieldPasswordErrorPreview() = PreviewColumn {
    var v by remember { mutableStateOf(FormsPreviewText.PASSWORD) }
    LabeledTextField(
        label = FormsPreviewText.Label.PASSWORD,
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Password,
        placeholder = FormsPreviewText.DOTS,
        error = FormsPreviewText.Error.PASSWORD,
        imeAction = ImeAction.Done,
        spacerAfter = false,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}