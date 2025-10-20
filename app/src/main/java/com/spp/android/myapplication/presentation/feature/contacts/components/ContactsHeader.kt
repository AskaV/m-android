package com.spp.android.myapplication.presentation.feature.contacts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme
import com.spp.android.myapplication.presentation.texts.AppText

@Composable
fun ContactsHeader(
    title: String = AppText.Contacts.TITLE.text(),
    onBack: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    showAddHeaderRow: Boolean,
    onAddContactsClick: () -> Unit = {},
    reserveAddRowSpace: Boolean = false

) {
    val pad = dimensionResource(id = R.dimen.spacer_medium)
    val spaceM = dimensionResource(id = R.dimen.spacer_medium)
    val addRowMinHeight = dimensionResource(id = R.dimen.spacer_large)

    Surface(
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier.fillMaxWidth().padding(horizontal = pad, vertical = spaceM)
            ) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onSearchClick, modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }

            if (showAddHeaderRow) {
                Text(
                    text = AppText.Contacts.ADD.text(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth().heightIn(min = addRowMinHeight)
                        .padding(vertical = spaceM, horizontal = pad)
                        .clickable(onClick = onAddContactsClick)
                )
            } else if (reserveAddRowSpace) {
                Spacer(
                    Modifier.fillMaxWidth().height(addRowMinHeight)
                        .padding(vertical = spaceM, horizontal = pad)
                )
            }
        }
    }
}

@PreviewPhones
@Composable
private fun ContactsHeaderPreviewDefault() {
    MyApplicationTheme {
        ContactsHeader(
            title = "Contacts", showAddHeaderRow = true
        )
    }
}

@PreviewPhones
@Composable
private fun ContactsHeaderPreviewUsers() {
    MyApplicationTheme {
        ContactsHeader(
            title = "Users", showAddHeaderRow = false
        )
    }
}