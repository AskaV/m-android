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
import com.spp.android.myapplication.presentation.designsystem.preview.ContactPreviewText
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

@Composable
fun ContactsList(
    modifier: Modifier = Modifier,
    items: List<ContactUi>,
    onItemClick: (ContactUi) -> Unit,
    onDeleteClick: (ContactUi) -> Unit,
    contentPadding: PaddingValues = PaddingValues(dimensionResource(id = R.dimen.spacer_medium))
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
                ContactUi(
                    1, ContactPreviewText.Preview.NAME1, ContactPreviewText.Preview.SUBTITLE1
                ),
                ContactUi(
                    2, ContactPreviewText.Preview.NAME2, ContactPreviewText.Preview.SUBTITLE2
                ),
                ContactUi(
                    3, ContactPreviewText.Preview.NAME3, ContactPreviewText.Preview.SUBTITLE3
                ),
                ContactUi(
                    4, ContactPreviewText.Preview.NAME4, ContactPreviewText.Preview.SUBTITLE4
                ),
                ContactUi(
                    5, ContactPreviewText.Preview.NAME5, ContactPreviewText.Preview.SUBTITLE5
                ),
                ContactUi(
                    6, ContactPreviewText.Preview.NAME6, ContactPreviewText.Preview.SUBTITLE6
                ),
            )
        }
        ContactsList(items = data, onItemClick = {}, onDeleteClick = { data.remove(it) })
    }
}