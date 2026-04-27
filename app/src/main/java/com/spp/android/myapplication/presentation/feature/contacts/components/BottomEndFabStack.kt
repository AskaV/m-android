package com.spp.android.myapplication.presentation.feature.contacts.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.feature.components.ActionFab

@Composable
fun BottomEndFabStack(
    modifier: Modifier = Modifier,
    showScrollTop: Boolean,
    showMassAction: Boolean,
    massActionIconRes: Int,
    massActionContentDescription: String,
    onScrollTop: () -> Unit,
    onMassAction: () -> Unit,
    hideScrollWhenMassAction: Boolean = false,
) {
    val topVisible =
        if (hideScrollWhenMassAction) showScrollTop && !showMassAction else showScrollTop

    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.spacer_medium)),
        horizontalAlignment = Alignment.End,
    ) {
        AnimatedVisibility(visible = topVisible) {
            ActionFab(
                iconRes = R.drawable.ic_arrow_up,
                contentDescription = "Scroll to top",
                onClick = onScrollTop,
                alignment = Alignment.BottomEnd,
            )
        }

        if (topVisible && showMassAction) {
            Spacer(Modifier.height(dimensionResource(id = R.dimen.spacer_medium)))
        }

        AnimatedVisibility(visible = showMassAction) {
            ActionFab(
                iconRes = massActionIconRes,
                contentDescription = massActionContentDescription,
                onClick = onMassAction,
                alignment = Alignment.BottomEnd,
            )
        }
    }
}