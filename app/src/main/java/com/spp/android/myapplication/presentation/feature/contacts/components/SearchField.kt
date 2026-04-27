package com.spp.android.myapplication.presentation.feature.contacts.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    val height = dimensionResource(id = R.dimen.exit_button)
    val fieldText = MaterialTheme.colorScheme.inverseOnSurface

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.weight(1f).height(height),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius)),
            color = MaterialTheme.colorScheme.onSecondary,
            contentColor = fieldText,
        ) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = fieldText),
                cursorBrush = SolidColor(fieldText),
                modifier = Modifier.fillMaxWidth().height(height),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(height)
                            .padding(horizontal = dimensionResource(id = R.dimen.spacer_additional)),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        androidx.compose.foundation.layout.Box(modifier = Modifier.weight(1f)) {
                            if (query.isBlank()) {
                                Text(
                                    text = placeholder,
                                    color = fieldText.copy(alpha = 0.65f),
                                    style = MaterialTheme.typography.bodyLarge,
                                    maxLines = 1,
                                )
                            }
                            innerTextField()
                        }

                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = fieldText,
                        )
                    }
                },
            )
        }

        Spacer(Modifier.width(dimensionResource(id = R.dimen.spacer_small)))

        IconButton(onClick = onClose) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@PreviewPhones
@Composable
private fun ContactsSearchBarPreview() {
    MyApplicationTheme {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.spacer_medium)),
        ) {
            SearchField(
                query = "Ava",
                onQueryChange = {},
                onClose = {},
            )
        }
    }
}