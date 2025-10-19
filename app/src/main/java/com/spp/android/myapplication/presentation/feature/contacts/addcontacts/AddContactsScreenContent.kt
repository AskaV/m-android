package com.spp.android.myapplication.presentation.feature.contacts.addcontacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.feature.components.ActionFab
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactList
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactListBehavior
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactsHeader
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun AddContactsScreenContent(
    items: List<ContactUi>,
    selectedIds: Set<Int> = emptySet(),
    onBack: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onToggleSelect: (ContactUi) -> Unit = {},
    onMassAddClick: () -> Unit = {},
    onAddClick: (ContactUi) -> Unit = {},
    reserveAddRowSpace: Boolean = true,
    onRowClick: (ContactUi) -> Unit = {},
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollTop by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 } }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Column(Modifier.fillMaxSize()) {
            ContactsHeader(
                title = AppText.AddContactDetailed.TITLE1.text(),
                onBack = onBack,
                onSearchClick = onSearchClick,
                showAddHeaderRow = false,
                onAddContactsClick = {},
                reserveAddRowSpace = reserveAddRowSpace
            )

            ContactList(
                items = items,
                selectedIds = emptySet(),
                behavior = ContactListBehavior(
                    selectionEnabled = false,
                    showDeleteIcon = false,
                    trailingForRow = { contact ->
                        TextButton(onClick = { onAddClick(contact) }) {
                            Text("Add", color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                painter = painterResource(R.drawable.ic_add),
                                contentDescription = "Add",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }),
                onItemClick = onRowClick,
                onItemLongClick = { /* not used */ },
                onDeleteClick = {},
                state = listState,
                contentPadding = PaddingValues(
                    start = pad, end = pad, top = spaceM, bottom = spaceL
                ),
                modifier = Modifier.fillMaxSize()
            )
        }

        AnimatedVisibility(
            visible = selectedIds.isNotEmpty(), modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.ic_add,
                contentDescription = "Add selected",
                onClick = onMassAddClick,
                alignment = Alignment.BottomEnd
            )
        }

        AnimatedVisibility(
            visible = showScrollTop, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.ic_arrow_up,
                contentDescription = "Scroll to top",
                onClick = { scope.launch { listState.animateScrollToItem(0) } },
                alignment = Alignment.BottomStart
            )
        }
    }
}

@PreviewPhones
@Composable
private fun AddContactsContentPreviewDefault() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AddContactsScreenContent(
                items = demoUsers(), selectedIds = emptySet()
            )
        }
    }
}

@PreviewPhones
@Composable
private fun AddContactsContentPreviewSelected() {
    val users = demoUsers()
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            AddContactsScreenContent(
                items = users, selectedIds = setOf(users[1].id, users[3].id)
            )
        }
    }
}