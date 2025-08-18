package com.spp.android.myapplication.screens.fragment.contact

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.spp.android.myapplication.R
import com.spp.android.myapplication.screens.util.extensions.LoadWithGlide

@Composable
fun ContactItem(
    contact: Contact,
    avatarTransitionName: String,
    onAvatarViewReady: (ImageView) -> Unit,
    isInSelectionMode: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.spacer_medium),
                vertical = dimensionResource(id = R.dimen.spacer_small)
            )
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .background(
                when {
                    isSelected -> MaterialTheme.colorScheme.secondary
                    isInSelectionMode -> MaterialTheme.colorScheme.secondary
                    else -> Color.Transparent
                }
            )
            .padding(dimensionResource(id = R.dimen.spacer_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isInSelectionMode) {
            val context = LocalContext.current
            val size = 20.dp

            val drawable = remember(isSelected) {
                AppCompatResources.getDrawable(context, R.drawable.selector_contact_checkbox)
                    ?.mutate()?.apply {
                        state = if (isSelected)
                            intArrayOf(android.R.attr.state_checked)
                        else
                            intArrayOf()
                    }
            }

            drawable?.let { d ->

                val bmp = remember(d, isSelected) { d.toBitmap().asImageBitmap() }
                Image(
                    bitmap = bmp,
                    contentDescription = null,
                    colorFilter = if (!isSelected) ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                    else null,
                    modifier = Modifier
                        .padding(end = dimensionResource(id = R.dimen.spacer_small))
                        .size(size)
                )
            }
        }

        LoadWithGlide(
            url = contact.avatarUrl,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.contacts_avatar_size))
                .clip(CircleShape),
            transitionName = avatarTransitionName,
            onViewReady = onAvatarViewReady
        )

        Column(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.spacer_medium))
                .weight(1f)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                text = contact.position,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        if (!isInSelectionMode) {
            AppCompatResources.getDrawable(context, R.drawable.recycle_bin)?.let { bin ->
                IconButton(onClick = onDeleteClick) {
                    Image(
                        painter = rememberDrawablePainter(bin),
                        contentDescription = stringResource(R.string.wastebasket_description),
                        modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
                    )
                }
            }
        }
    }
}
