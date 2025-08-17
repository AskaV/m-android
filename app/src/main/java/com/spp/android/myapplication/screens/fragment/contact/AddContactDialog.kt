package com.spp.android.myapplication.screens.fragment.contact

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.spp.android.myapplication.R

@Composable
fun AddContactDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onAddContact: (Contact) -> Unit
) {
    if (!showDialog) return

    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.add_contacts_text)) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.add_contacts_name)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = position,
                    onValueChange = { position = it },
                    label = { Text(stringResource(R.string.add_contacts_position)) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && position.isNotBlank()) {
                        onAddContact(
                            Contact(
                                name = name,
                                position = position,
                                avatarUrl = "https://i.pravatar.cc/150?img=${(1..70).random()}"
                            )
                        )
                        onDismiss()
                    }
                }
            ) { Text(stringResource(R.string.add_contacts_save_text)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.add_contacts_cancel_text))
            }
        }
    )
}