package com.spp.android.myapplication.presentation.designsystem.components.inputs.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind
import com.spp.android.myapplication.presentation.designsystem.preview.FormsPreviewText

@Suppress("LongParameterList")
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    kind: FieldKind,
    placeholder: String? = null,
    isError: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: (() -> Unit)? = null,
    trailingIcon: (@Composable (() -> Unit))? = null,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onBackground
    )

) {
    val padding = dimensionResource(R.dimen.spacer_small)
    val visual = kind.visualTransformation

    Column(modifier.fillMaxWidth()) {

        Box(Modifier.fillMaxWidth()) {
            if (placeholder != null && value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier
                        .padding(start = padding, top = padding, end = padding, bottom = padding)
                        .fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = textStyle,
                    visualTransformation = visual,
                    keyboardOptions = kind.keyboardOptions.copy(imeAction = imeAction),
                    keyboardActions = KeyboardActions(
                        onDone = { onImeAction?.invoke() },
                        onNext = { onImeAction?.invoke() }
                    ),
                    cursorBrush = SolidColor(textStyle.color),
                    modifier = Modifier
                        .weight(1f)
                        .padding(
                            start = padding,
                            top = padding,
                            bottom = padding,
                            end = if (trailingIcon != null) 0.dp else padding
                        )
                )

                if (trailingIcon != null) {
                    Box(Modifier.padding(end = padding, top = padding, bottom = padding)) {
                        trailingIcon.invoke()
                    }
                }
            }
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface)
        )
    }
}

@PreviewPhones
@Composable
fun AppTextFieldEmailPreview() = PreviewColumn {
    var v by remember { mutableStateOf(FormsPreviewText.EMAIL) }
    AppTextField(
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Email,
        placeholder = FormsPreviewText.EMAIL,
        imeAction = ImeAction.Next,
        isError = false,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@PreviewPhones
@Composable
fun AppTextFieldPasswordErrorPreview() = PreviewColumn {
    var v by remember { mutableStateOf(FormsPreviewText.PASSWORD) }
    AppTextField(
        value = v,
        onValueChange = { v = it },
        kind = FieldKind.Password,
        placeholder = FormsPreviewText.DOTS,
        imeAction = ImeAction.Done,
        isError = true,
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}