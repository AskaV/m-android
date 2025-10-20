package com.spp.android.myapplication.presentation.feature.contacts.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.spp.android.myapplication.R
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SwipeToDeleteContainer(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    deleteThresholdDp: Float = 100f,
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    val density = LocalDensity.current
    val deleteThresholdPx = with(density) { deleteThresholdDp.dp.toPx() }
    val showBg by remember { derivedStateOf { offsetX.value < -1f } }

    Box(
        modifier = modifier.clip(RoundedCornerShape(dimensionResource(id = R.dimen.button_corner_radius)))
    ) {
        Box(
            modifier = Modifier.matchParentSize().alpha(if (showBg) 1f else 0f)
                .background(MaterialTheme.colorScheme.error)
                .padding(horizontal = dimensionResource(id = R.dimen.spacer_large)),
            contentAlignment = Alignment.CenterEnd,

            ) {
            Icon(
                painter = painterResource(R.drawable.recycle_bin),
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.onError
            )
        }

        Box(modifier = Modifier.offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .pointerInput(enabled) {
                if (!enabled) return@pointerInput
                detectHorizontalDragGestures(onHorizontalDrag = { _, dragAmount ->
                    val newX = (offsetX.value + dragAmount).coerceAtMost(0f)
                    scope.launch { offsetX.snapTo(newX) }
                }, onDragEnd = {
                    scope.launch {
                        if (abs(offsetX.value) >= deleteThresholdPx) {
                            val further = offsetX.value - deleteThresholdPx
                            offsetX.animateTo(
                                targetValue = further - deleteThresholdPx,
                                animationSpec = tween(180)
                            )
                            onDelete()
                            offsetX.snapTo(0f)
                        } else {
                            offsetX.animateTo(0f, animationSpec = tween(180))
                        }
                    }
                }, onDragCancel = {
                    scope.launch { offsetX.animateTo(0f, tween(180)) }
                })
            }) {
            content()
        }
    }
}