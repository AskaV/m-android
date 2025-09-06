package com.spp.android.myapplication.presentation.feature.contacts

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewScreenEdgeToEdge
import com.spp.android.myapplication.presentation.designsystem.preview.ProfilePreviewText
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactCardOutlined
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi

@Composable
fun ContactsScreenContent(
    items: List<ContactUi>,
    onBack: () -> Unit,
    onSearchClick: () -> Unit,
    onAddContactsClick: () -> Unit,
    onContactClick: (ContactUi) -> Unit,
    onDeleteClick: (ContactUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val pad = dimensionResource(id = R.dimen.padding_screen)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val spaceL = dimensionResource(id = R.dimen.spacer_large)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
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
                        text = ProfilePreviewText.ContactProfile.CONTACTS,
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
                    text = ProfilePreviewText.ContactProfile.ADD_CONTACTS,
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
                contentPadding = PaddingValues(
                    start = pad, end = pad, top = spaceM, bottom = spaceL
                )
            ) {

                items(items, key = { it.id }) { c ->
                    ContactCardOutlined(
                        contact = c,
                        onClick = onContactClick,
                        onDeleteClick = onDeleteClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(spaceM))
                }
            }
        }
    }
}


@PreviewPhones
@Composable
private fun ContactsScreenContentPreview() = PreviewScreenEdgeToEdge {
    val demo = remember {
        listOf(
            ContactUi("1", "Ava Smith", "Photograph"),
            ContactUi("2", "Jessie Brown", "Actress"),
            ContactUi("3", "Jackie Taylor", "Financier"),
            ContactUi("4", "Jenny Walker", "Make-up artist"),
            ContactUi("5", "Freddy Harris", "Secretary"),
            ContactUi("6", "Annie King", "Nurse"),
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