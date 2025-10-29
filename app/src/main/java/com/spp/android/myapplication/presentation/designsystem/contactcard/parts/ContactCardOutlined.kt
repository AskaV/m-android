package com.spp.android.myapplication.presentation.designsystem.contactcard.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewColumn
import com.spp.android.myapplication.presentation.designsystem.preview.PreviewPhones
import com.spp.android.myapplication.presentation.designsystem.preview.demoUsers
import com.spp.android.myapplication.presentation.feature.components.SelectionCheck

data class ContactUi(
    val id: Int,
    val name: String,
    val subtitle: String = "",
    val avatarUrl: String? = "",
    val transitionName: String? = "",
    val phone: String = ""
)

@Composable
fun ContactCardOutlined(
    modifier: Modifier = Modifier,
    contact: ContactUi,
    onClick: (ContactUi) -> Unit = {},
    onLongClick: (ContactUi) -> Unit = {},
    showSelectionControl: Boolean = false,
    trailing: (@Composable RowScope.() -> Unit)? = null,
    showDeleteIcon: Boolean = false,
    selected: Boolean = false,
    onDeleteClick: (ContactUi) -> Unit = {}
) {
    val corner = dimensionResource(id = R.dimen.button_corner_radius)
    val borderW = dimensionResource(id = R.dimen.button_border_width)
    val spaceS = dimensionResource(id = R.dimen.spacer_small)

    val clickableMod = modifier.combinedClickable(
        onClick = { onClick(contact) },
        onLongClick = { onLongClick(contact) })

    Surface(
        modifier = clickableMod,
        shape = RoundedCornerShape(corner),
        color = if (selected) MaterialTheme.colorScheme.surfaceVariant
        else MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(borderW, MaterialTheme.colorScheme.onSurface),
    ) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = spaceS).heightIn(min = 72.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showSelectionControl) {
                SelectionCheck(
                    selected = selected, modifier = Modifier.padding(end = spaceS)
                )
            }

            Image(
                painter = painterResource(R.drawable.baseline_account_circle_avatar),
                contentDescription = null,
                modifier = Modifier.size(56.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(spaceS))

            Column(
                Modifier.weight(1f).padding(end = spaceS)
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

            if (trailing != null) {
                trailing()
            } else if (showDeleteIcon) {
                IconButton(onClick = { onDeleteClick(contact) }) {
                    Icon(
                        painter = painterResource(R.drawable.recycle_bin),
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@PreviewPhones
@Composable
private fun ContactCardOutlinedPreview() {
    PreviewColumn {
        ContactCardOutlined(
            contact = demoUsers()[0],
        )
    }
}