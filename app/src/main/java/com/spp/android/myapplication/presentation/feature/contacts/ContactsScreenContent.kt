package com.spp.android.myapplication.presentation.feature.contacts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactCardOutlined
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.ContactPreviewText
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.texts.AppText
import com.spp.android.myapplication.presentation.feature.components.ActionFab
import kotlinx.coroutines.launch

@Composable
fun ContactsScreenContent(
    modifier: Modifier = Modifier,
    items: List<ContactUi>,
    onBack: () -> Unit,
    onSearchClick: () -> Unit,
    onAddContactsClick: () -> Unit,
    onContactClick: (ContactUi) -> Unit,
    onDeleteClick: (ContactUi) -> Unit,
    onBulkDeleteClick: () -> Unit = {},
    onScrollTopClick: () -> Unit = {},
    showRecycleBin: Boolean = false,
    onContactLongClick: (ContactUi) -> Unit = {},
    selectedIds: Set<String> = emptySet(),
    isSelectionMode: Boolean = showRecycleBin
    ) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val showScrollTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = pad, vertical = spaceM)
                    ) {
                        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(
                            text = AppText.Contacts.TITLE.text(),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.align(Alignment.Center)
                        )
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }

                    Text(
                        text = AppText.Contacts.ADD.text(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = spaceM, horizontal = pad)
                            .clickable(onClick = onAddContactsClick)
                    )
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(
                        start = pad, end = pad, top = spaceM, bottom = spaceL
                    )
                ) {
                    items(items, key = { it.id }) { c ->
                        ContactCardOutlined(
                            contact = c,
                            onClick = { onContactClick(c) },
                            onLongClick = { onContactLongClick(c) },
                            selected = selectedIds.contains(c.id),
                            showSelectionControl = isSelectionMode,
                            showDeleteIcon = !isSelectionMode,
                            modifier = Modifier.fillMaxWidth(),
                            onDeleteClick = { onDeleteClick(c) }
                        )
                        Spacer(Modifier.height(spaceM))
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showRecycleBin,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.recycle_bin,
                contentDescription = "Delete selected",
                onClick = onBulkDeleteClick,
                alignment = Alignment.BottomEnd
            )
        }

        AnimatedVisibility(
            visible = showScrollTop,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            ActionFab(
                iconRes = R.drawable.ic_arrow_up,
                contentDescription = "Scroll to top",
                onClick = {
                    scope.launch { listState.animateScrollToItem(0) }
                },
                alignment = Alignment.BottomStart
            )
        }
    }
}


@PreviewPhones
@Composable
private fun ContactsScreenContentPreview() = PreviewScreenEdgeToEdge {
    val demo = remember {
        listOf(
            ContactUi("1", ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1),
            ContactUi("2", ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2),
            ContactUi("3", ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3),
            ContactUi("4", ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4),
            ContactUi("5", ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5),
            ContactUi("6", ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6),
        )
    }
    ContactsScreenContent(
        items = demo,
        onBack = {},
        onSearchClick = {},
        onAddContactsClick = {},
        onContactClick = {},
        onDeleteClick = {}
    )
}