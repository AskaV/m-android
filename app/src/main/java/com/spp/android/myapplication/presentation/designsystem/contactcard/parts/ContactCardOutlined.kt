package com.spp.android.myapplication.presentation.designsystem.contactcard.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.ContactText
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones

data class ContactUi(
    val id: String,
    val name: String,
    val subtitle: String,
    val avatarUrl: String? = null,
    val transitionName: String? = null
)

@Composable
fun ContactCardOutlined(
    contact: ContactUi,
    onClick: (ContactUi) -> Unit,
    onDeleteClick: (ContactUi) -> Unit,
    modifier: Modifier = Modifier
) {
    val corner = dimensionResource(id = R.dimen.button_corner_radius)
    val borderW = dimensionResource(id = R.dimen.button_border_width)
    val spaceS = dimensionResource(id = R.dimen.spacer_small)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(corner),
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(borderW, MaterialTheme.colorScheme.onSurface)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .clickable { onClick(contact) }
                .padding(horizontal = spaceS)
                .heightIn(min = 72.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_account_circle_avatar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(spaceS))

            Column(
                Modifier
                    .weight(1f)
                    .padding(end = spaceS)
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = contact.subtitle,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = { onDeleteClick(contact) }) {
                Icon(
                    painter = painterResource(R.drawable.recycle_bin),
                    contentDescription = ContactText.DELETE,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@PreviewPhones
@Composable
private fun ContactCardOutlinedPreview() {
    PreviewColumn {
        ContactCardOutlined(
            contact = ContactUi(
                id = "1",
                name = ContactText.Preview.NAME1,
                subtitle = ContactText.Preview.SUBTITLE1,
                avatarUrl = null
            ),
            onClick = {},
            onDeleteClick = {}
        )
    }
}