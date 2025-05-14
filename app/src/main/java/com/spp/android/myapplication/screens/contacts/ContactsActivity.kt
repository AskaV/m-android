package com.spp.android.myapplication.screens.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.spp.android.myapplication.R
import com.spp.android.myapplication.ui.preview.PreviewConfig
import com.spp.android.myapplication.ui.theme.MyApplicationTheme


class ContactsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val themePref = getSharedPreferences("user_prefs", MODE_PRIVATE)
            .getString("theme_pref", "system") ?: "system"

        setContent {
            MyApplicationTheme(themePref = themePref) {
                ContactsScreen(onValidLogin = {})
            }
        }
    }
}

@Preview(
    showBackground = true,
    name = "ContactsScreenPreview",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO,
    device = "spec:width=${PreviewConfig.FIGMA_SCREEN_WIDTH}px,height=${PreviewConfig.FIGMA_SCREEN_HEIGHT}px,dpi=${PreviewConfig.FIGMA_SCREEN_DPI}"
)

@Composable
fun ContactsScreenPreview() {
    MyApplicationTheme(themePref = "system") { // colored or system
        ContactsScreen(onValidLogin = {})
    }
}

@Composable
fun ContactsScreen(
    modifier: Modifier = Modifier,
    onValidLogin: (String) -> Unit,
    viewModel: ContactsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .weight(0.2f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = dimensionResource(id = R.dimen.spacer_medium))

        ) {

            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.align(Alignment.TopStart)
                    .padding(top = dimensionResource(id = R.dimen.spacer_medium))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                )
            }

            Text(
                text = stringResource(R.string.contacts_text),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(top = dimensionResource(id = R.dimen.spacer_medium))
            )

            IconButton(
                onClick = { /* TODO */ },
                modifier = Modifier.align(Alignment.TopEnd)
                    .padding(top = dimensionResource(id = R.dimen.spacer_medium))

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search_button),
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                )
            }

            Text(
                text = stringResource(R.string.add_contacts_text),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(bottom = dimensionResource(id = R.dimen.spacer_large))
            )
        }
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val viewModel: ContactsViewModel = viewModel()
            val contacts by viewModel.contacts.collectAsState()

            LazyColumn(
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.spacer_small))
            ) {
                items(contacts.size) { index ->
                    ContactItem(contact = contacts[index]) {
                        // TODO
                    }
                }
            }
        }
    }
}