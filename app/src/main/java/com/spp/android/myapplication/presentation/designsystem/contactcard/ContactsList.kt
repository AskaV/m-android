package com.spp.android.myapplication.presentation.designsystem.contactcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactCardOutlined
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactUi
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun ContactsList(
    items: List<ContactUi>,
    onItemClick: (ContactUi) -> Unit,
    onDeleteClick: (ContactUi) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = R.dimen.padding_screen))
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacer_medium))
    ) {
        items(items, key = { it.id }) { c ->
            ContactCardOutlined(
                contact = c,
                onClick = onItemClick,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@PreviewPhones
@Composable
private fun ContactsListPreview() {
    Surface(color = MaterialTheme.colorScheme.surface) {
        val data = remember {
            mutableStateListOf(
                ContactUi("1", "Ava Smith", "Photograph"),
                ContactUi("2", "Jessie Brown", "Actress"),
                ContactUi("3", "Jackie Taylor", "Financier"),
                ContactUi("4", "Jenny Walker", "Make-up artist"),
                ContactUi("5", "Freddy Harris", "Secretary"),
                ContactUi("6", "Annie King", "Nurse"),
            )
        }
        ContactsList(
            items = data,
            onItemClick = {},
            onDeleteClick = { data.remove(it) }
        )
    }
}