package com.spp.android.myapplication.presentation.feature.contacts.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.components.ActionFab
import com.spp.android.myapplication.presentation.feature.contacts.components.BottomEndFabStack
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactList
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactListBehavior
import com.spp.android.myapplication.presentation.feature.contacts.components.ContactsHeader
import com.spp.android.myapplication.presentation.feature.contacts.components.EmptySearchResult
import com.spp.android.myapplication.presentation.texts.AppText
import kotlinx.coroutines.launch

@Composable
fun ContactsScreenContent(
    modifier: Modifier = Modifier,
    items: List<Contact>,
    onBack: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onAddContactClick: () -> Unit = {},

    onAddContactsClick: () -> Unit = {},
    onContactClick: (Contact) -> Unit = {},
    onDeleteClick: (Contact) -> Unit = {},
    onBulkDeleteClick: () -> Unit = {},
    showRecycleBin: Boolean = false,
    onContactLongClick: (Contact) -> Unit = {},
    selectedIds: Set<Int> = emptySet(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isSelectionMode: Boolean = showRecycleBin,
    isSearchOpen: Boolean = false,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearchClose: () -> Unit = {},

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
            .padding(contentPadding),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            ContactsHeader(
                title = AppText.Contacts.TITLE.text(),
                onBack = onBack,
                onSearchClick = onSearchClick,
                showAddHeaderRow = true,
                onAddContactsClick = onAddContactsClick,
                onAddContactClick = onAddContactClick,
                isSearchOpen = isSearchOpen,
                query = query,
                onQueryChange = onQueryChange,
                onSearchClose = onSearchClose,
            )
            if (items.isEmpty() && isSearchOpen && query.isNotBlank()) {
                EmptySearchResult()

            } else {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ContactList(
                        items = items,
                        selectedIds = selectedIds,
                        behavior = ContactListBehavior(
                            selectionEnabled = isSelectionMode,
                            showDeleteIcon = !isSelectionMode,
                        ),
                        onItemClick = onContactClick,
                        onItemLongClick = onContactLongClick,
                        onDeleteClick = onDeleteClick,
                        state = listState,
                        contentPadding = PaddingValues(
                            start = pad,
                            end = pad,
                            top = pad,
                            bottom = spaceL,
                        ),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }

        BottomEndFabStack(
            modifier = Modifier.align(Alignment.BottomEnd),
            showScrollTop = showScrollTop,
            showMassAction = showRecycleBin,
            massActionIconRes = R.drawable.recycle_bin,
            massActionContentDescription = "Delete selected",
            onScrollTop = { scope.launch { listState.animateScrollToItem(0) } },
            onMassAction = onBulkDeleteClick,
            hideScrollWhenMassAction = false,
        )
    }
}

@PreviewPhones
@Composable
private fun ContactsScreenContentPreview() =
    PreviewScreenEdgeToEdge { ContactsScreenContent(items = demoUsers()) }
