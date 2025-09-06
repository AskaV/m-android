package com.spp.android.myapplication.presentation.designsystem.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.spp.android.myapplication.R
import com.spp.android.myapplication.presentation.designsystem.theme.AppTheme
import com.spp.android.myapplication.presentation.designsystem.theme.MyApplicationTheme

object PreviewDevices {
    const val FIGMA = "spec:width=360px,height=720px,dpi=160"
    const val MOTO = "spec:width=1224px,height=2992px,dpi=480"
}

@Preview(name = "1_MOTO_Light_Preview", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO, device = PreviewDevices.MOTO)
@Preview(name = "2_MOTO_Dark_Preview", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES, device = PreviewDevices.MOTO)
@Preview(name = "3_MOTO_Colored_Preview", showBackground = false, device = PreviewDevices.FIGMA)
annotation class PreviewMoto

@Preview(name = "1_FIGMA_Light_Preview", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO, device = PreviewDevices.FIGMA)
@Preview(name = "2_FIGMA_Dark_Preview", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES, device = PreviewDevices.FIGMA)
@Preview(name = "3_FIGMA_Colored_Preview", showBackground = false, device = PreviewDevices.FIGMA)
annotation class PreviewPhones


@Composable
fun AutoThemePreview(content: @Composable () -> Unit) {
    val cfg = LocalConfiguration.current
    val nightMask = cfg.uiMode and Configuration.UI_MODE_NIGHT_MASK
    val theme = when (nightMask) {
        Configuration.UI_MODE_NIGHT_YES -> AppTheme.DARK
        Configuration.UI_MODE_NIGHT_NO -> AppTheme.LIGHT
        Configuration.UI_MODE_NIGHT_UNDEFINED -> AppTheme.COLORED
        else -> AppTheme.SYSTEM
    }
    MyApplicationTheme(theme = theme) { content() }
}

@Composable
fun PreviewColumn(
    spacing: Dp = dimensionResource(id = R.dimen.spacer_additional),
    background: @Composable () -> Color = { MaterialTheme.colorScheme.background },
    content: @Composable ColumnScope.() -> Unit
) = AutoThemePreview {
    Surface(color = background()) {
        Column(
            Modifier.padding(dimensionResource(id = R.dimen.padding_screen)),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            content()
        }
    }
}

@Composable
fun PreviewRow(
    spacing: Dp = dimensionResource(id = R.dimen.spacer_additional),
    background: @Composable () -> Color = { MaterialTheme.colorScheme.background },
    content: @Composable RowScope.() -> Unit
) = AutoThemePreview {
    Surface(color = background()) {
        Row(
            Modifier.padding(dimensionResource(id = R.dimen.padding_screen)),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            content()
        }
    }
}

@Composable
fun PreviewScreenEdgeToEdge(
    content: @Composable () -> Unit
) = AutoThemePreview {
    Surface(color = MaterialTheme.colorScheme.background) {
        Box(Modifier.fillMaxSize()) { content() }
    }
}