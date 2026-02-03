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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.domain.model.Contact
import com.spp.android.myapplication.presentation.designsystem.contactcard.parts.ContactCardOutlined
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers

@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    items: List<Contact> = emptyList(),
    onItemClick: (Contact) -> Unit = {},
    onDeleteClick: (Contact) -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = R.dimen.spacer_medium)),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacer_medium)),
    ) {
        items(items, key = { it.id }) { c ->
            ContactCardOutlined(
                contact = c,
                onClick = onItemClick,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@PreviewPhones
@Composable
private fun ContactsListPreview() {
    Surface(color = MaterialTheme.colorScheme.surface) {
        ContactsList(items = demoUsers())
    }
}
