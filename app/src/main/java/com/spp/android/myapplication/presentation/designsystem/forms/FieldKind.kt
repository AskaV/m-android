package com.spp.android.myapplication.presentation.designsystem.forms

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.spp.android.myapplication.presentation.designsystem.preview.FormsPreviewText

sealed class FieldKind(
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation,
    val imeAction: ImeAction = ImeAction.Next,
    val label: String,
    val placeholderPreview: String = ""
) {
    data object Email : FieldKind(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        visualTransformation = VisualTransformation.None,
        imeAction = ImeAction.Next,
        label = FormsPreviewText.Label.EMAIL,
        placeholderPreview = FormsPreviewText.EMAIL
    )

    data object Password : FieldKind(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation(),
        imeAction = ImeAction.Done,
        label = FormsPreviewText.Label.PASSWORD,
        placeholderPreview = FormsPreviewText.DOTS
    )

    data object Username : FieldKind(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        visualTransformation = VisualTransformation.None,
        imeAction = ImeAction.Next,
        label = FormsPreviewText.Label.USERNAME,
        placeholderPreview = FormsPreviewText.USERNAME
    )

    data object Phone : FieldKind(
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = VisualTransformation.None,
        imeAction = ImeAction.Next,
        label = FormsPreviewText.Label.PHONE,
        placeholderPreview = FormsPreviewText.PHONE
    )
}