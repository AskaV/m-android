package com.spp.android.myapplication.presentation.feature.contacts.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactCardOutlined
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme

data class ContactListBehavior(
    val selectionEnabled: Boolean = false,
    val showDeleteIcon: Boolean = false,
    val trailingForRow: (@Composable RowScope.(Contact) -> Unit)? = null
)

@Composable
fun ContactList(
    modifier: Modifier = Modifier,
    items: List<Contact>,
    selectedIds: Set<Int> = emptySet(),
    behavior: ContactListBehavior,
    onItemClick: (Contact) -> Unit = {},
    onItemLongClick: (Contact) -> Unit = {},
    onDeleteClick: (Contact) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues()
) {

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(), state = state, contentPadding = contentPadding
        ) {
            items(items, key = { it.id }) { c ->
                SwipeToDeleteContainer(
                    enabled = !behavior.selectionEnabled, onDelete = { onDeleteClick(c) }) {
                    ContactCardOutlined(
                        contact = c,
                        onClick = { onItemClick(c) },
                        onLongClick = { onItemLongClick(c) },
                        selected = selectedIds.contains(c.id),
                        showSelectionControl = behavior.selectionEnabled,
                        showDeleteIcon = behavior.showDeleteIcon && behavior.trailingForRow == null,
                        trailing = behavior.trailingForRow?.let { tf -> { tf(c) } },
                        onDeleteClick = { onDeleteClick(c) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))
            }
        }
    }
}

@PreviewPhones
@Composable
private fun ContactListPreviewNormal() {
    MyApplicationTheme {
        ContactList(
            items = demoUsers(), behavior = ContactListBehavior(
                selectionEnabled = false, showDeleteIcon = true,
            ), contentPadding = PaddingValues(), modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewPhones
@Composable
private fun ContactListPreviewSelection() {
    MyApplicationTheme {
        val selected = remember { setOf(2, 4) }
        ContactList(
            items = demoUsers(), selectedIds = selected, behavior = ContactListBehavior(
                selectionEnabled = true, showDeleteIcon = false,
            ), contentPadding = PaddingValues(), modifier = Modifier.fillMaxSize()
        )
    }
}