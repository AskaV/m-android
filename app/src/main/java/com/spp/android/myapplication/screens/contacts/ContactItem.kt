package com.spp.android.myapplication.screens.contacts

import android.widget.ImageView
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.screens.util.extensions.LoadWithGlide

@Composable
fun ContactItem(
    contact: Contact,
    avatarTransitionName: String,
    onAvatarViewReady: (ImageView) -> Unit,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(id = R.dimen.spacer_medium),
                vertical = dimensionResource(id = R.dimen.spacer_small)
            )
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.medium)
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .padding(dimensionResource(id = R.dimen.spacer_small)),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        LoadAvatarComposable(
//            url = contact.avatarUrl,
//            modifier = Modifier
//                .size(dimensionResource(id = R.dimen.contacts_avatar_size))
//                .clickable { onClick() }
//        )
        LoadWithGlide(
            url = contact.avatarUrl,
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.contacts_avatar_size))
                .clip(CircleShape)
                .clickable { onClick() },
            transitionName = avatarTransitionName,
            onViewReady = onAvatarViewReady
        )

        Column(
            modifier = Modifier
                .padding(start = dimensionResource(id = R.dimen.spacer_medium))
                .weight(1f)
        ) {
            Text(
                contact.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Text(
                contact.position,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(
                painter = painterResource(id = R.drawable.recycle_bin),
                contentDescription = stringResource(R.string.wastebasket_description),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(dimensionResource(id = R.dimen.spacer_large))
            )
        }
    }
}