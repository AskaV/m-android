package com.spp.android.myapplication.presentation.feature.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import com.spp.android.myapplication.presentation.designsystem.components.inputs.parts.LabeledTextField
import com.spp.android.myapplication.presentation.designsystem.forms.FieldKind

@Composable
fun BirthdateLabeledField(
    value: String,
    label: String,
    error: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    labelTextColor: Color = colorScheme.onSurface,
    valueTextColor: Color = colorScheme.onBackground,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Box(modifier = modifier) {
        LabeledTextField(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            value = value,
            onValueChange = {},
            kind = FieldKind.Username,
            placeholder = "YYYY-MM-DD",
            error = error,
            imeAction = ImeAction.Next,
            labelTextColor = labelTextColor,
            valueTextColor = valueTextColor,
            trailingIcon = {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = null)
                }
            },
        )

        Box(
            modifier = Modifier.matchParentSize().clickable { showDialog = true },
        )
    }

    if (showDialog) {
        DatePickerDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            TextButton(
                onClick = {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        onDateSelected(millisToIsoDate(millis))
                    }
                    showDialog = false
                },
            ) { Text("OK") }
        }, dismissButton = {
            TextButton(onClick = { showDialog = false }) { Text("Отмена") }
        }) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun millisToIsoDate(millis: Long): String {
    val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
    sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
    return sdf.format(java.util.Date(millis))
}
