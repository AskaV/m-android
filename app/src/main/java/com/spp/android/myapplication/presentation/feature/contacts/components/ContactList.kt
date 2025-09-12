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
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme

data class ContactListBehavior(
    val selectionEnabled: Boolean = false,
    val showDeleteIcon: Boolean = false,
    val trailingForRow: (@Composable RowScope.(ContactUi) -> Unit)? = null
)

@Composable
fun ContactList(
    modifier: Modifier = Modifier,
    items: List<ContactUi>,
    selectedIds: Set<String> = emptySet(),
    behavior: ContactListBehavior,
    onItemClick: (ContactUi) -> Unit,
    onItemLongClick: (ContactUi) -> Unit = {},
    onDeleteClick: (ContactUi) -> Unit = {},
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)

    Box(modifier = modifier) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            contentPadding = contentPadding
        ) {
            items(items, key = { it.id }) { c ->
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
                Spacer(Modifier.height(spaceM))
            }
        }
    }
}

@PreviewPhones
@Composable
private fun ContactListPreviewNormal() {
    MyApplicationTheme {

        val items = remember { demoUsers() }
        ContactList(
            items = items,
            selectedIds = emptySet(),
            behavior = ContactListBehavior(
                selectionEnabled = false,
                showDeleteIcon = true,
                trailingForRow = null
            ),
            onItemClick = {},
            onItemLongClick = {},
            onDeleteClick = {},
            contentPadding = PaddingValues(),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@PreviewPhones
@Composable
private fun ContactListPreviewSelection() {
    MyApplicationTheme {

        val items = remember { demoUsers() }
        val selected = remember { setOf("2", "4") }
        ContactList(
            items = items,
            selectedIds = selected,
            behavior = ContactListBehavior(
                selectionEnabled = true,
                showDeleteIcon = false,
                trailingForRow = null
            ),
            onItemClick = {},
            onItemLongClick = {},
            onDeleteClick = {},
            contentPadding = PaddingValues(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
