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
import com.spp.android.myapplication.presentation.designsystem.preview.ContactText
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
                ContactUi("1", ContactText.Preview.NAME1, ContactText.Preview.SUBTITLE1),
                ContactUi("2", ContactText.Preview.NAME2, ContactText.Preview.SUBTITLE2),
                ContactUi("3", ContactText.Preview.NAME3, ContactText.Preview.SUBTITLE3),
                ContactUi("4", ContactText.Preview.NAME4, ContactText.Preview.SUBTITLE4),
                ContactUi("5", ContactText.Preview.NAME5, ContactText.Preview.SUBTITLE5),
                ContactUi("6", ContactText.Preview.NAME6, ContactText.Preview.SUBTITLE6),
            )
        }
        ContactsList(
            items = data,
            onItemClick = {},
            onDeleteClick = { data.remove(it) }
        )
    }
}