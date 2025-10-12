package com.spp.android.myapplication.presentation.designsystem.imageload.parts

import androidx.compose.ui.unit.Dp

internal fun computeColumns(
    maxWidth: Dp, minTile: Dp, spacing: Dp, maxColumns: Int
): Int {
    val cols = ((maxWidth + spacing) / (minTile + spacing)).toInt()
    return cols.coerceIn(1, maxColumns)
}

internal fun computeTileSize(
    maxWidth: Dp, columns: Int, spacing: Dp
): Dp {
    val gaps = (columns - 1).coerceAtLeast(0)
    return (maxWidth - spacing * gaps) / columns
}