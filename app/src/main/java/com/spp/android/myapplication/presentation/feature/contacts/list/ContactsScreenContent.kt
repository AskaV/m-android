package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.components.ActionFab
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactList
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactListBehavior
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactsHeader
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun ContactsScreenContent(
    modifier: Modifier = Modifier,
    items: List<ContactUi>,
    onBack: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAddContactsClick: () -> Unit = {},
    onContactClick: (ContactUi) -> Unit = {},
    onDeleteClick: (ContactUi) -> Unit = {},
    onBulkDeleteClick: () -> Unit = {},
    showRecycleBin: Boolean = false,
    onContactLongClick: (ContactUi) -> Unit = {},
    selectedIds: Set<Int> = emptySet(),
    isSelectionMode: Boolean = showRecycleBin
) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ContactsHeader(
                title = AppText.Contacts.TITLE.text(),
                onBack = onBack,
                onSearchClick = onSearchClick,
                showAddHeaderRow = true,
                onAddContactsClick = onAddContactsClick
            )

            Surface(
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxSize()
            ) {
                ContactList(
                    items = items,
                    selectedIds = selectedIds,
                    behavior = ContactListBehavior(
                        selectionEnabled = isSelectionMode, showDeleteIcon = !isSelectionMode
                    ),
                    onItemClick = onContactClick,
                    onItemLongClick = onContactLongClick,
                    onDeleteClick = onDeleteClick,
                    state = listState,
                    contentPadding = PaddingValues(
                        start = pad, end = pad, top = pad, bottom = spaceL
                    ),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        AnimatedVisibility(
            visible = showRecycleBin, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.recycle_bin,
                onClick = onBulkDeleteClick,
                alignment = Alignment.BottomEnd
            )
        }

        AnimatedVisibility(
            visible = showScrollTop, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.ic_arrow_up,
                onClick = { scope.launch { listState.animateScrollToItem(0) } },
                alignment = Alignment.BottomStart
            )
        }
    }
}


@PreviewPhones
@Composable
private fun ContactsScreenContentPreview() =
    PreviewScreenEdgeToEdge { ContactsScreenContent(items = demoUsers()) }