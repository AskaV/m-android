package com.spp.android.myapplication.presentation.feature.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SelectionCheck(
    modifier: Modifier = Modifier, selected: Boolean
) {
    val size = 20.dp
    val borderW = 2.dp
    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant
    val checkTint = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier.size(size).clip(CircleShape).border(borderW, borderColor, CircleShape)
            .background(MaterialTheme.colorScheme.surface), contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = checkTint,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}