package com.spp.android.myapplication.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppTheme { SYSTEM, LIGHT, DARK, COLORED }

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,                   // Main background color for the entire screen.                                 BLUE 0xFF242235
    onBackground = TextPrimaryLight,                // Color of content (text/icons) displayed on the background.                   WHITE 0xFFFAFAFA

    primary = AccentLight,                          // Primary accent color — used for buttons, active elements, toggles, etc.      PINK 0xFFFF6B6B
    onPrimary = TextPrimaryLight,                   // Color of text and icons displayed on top of the primary color.               WHITE 0xFFFAFAFA

    surface = TextPrimaryLight,                     // Surface color — used for cards, sheets, panels, and other containers.        WHITE 0xFFFAFAFA
    onSurface = TextSecondaryLight,                 // Color of text and icons displayed on top of surfaces.

    secondary = TextFontSecondaryAccent,            // Secondary accent — typically used for chips, icons, and subtle highlights.
    onSecondary = TextTertiaryLight,                // Color used on top of the secondary color — typically for text inside chips or badges.

    error = ErrorLight,                             // Error color — used for validation messages, error states, and indicators.
    //onError = TextPrimaryLight                    // Color of text/icons displayed on top of the error color.

    tertiary = PanelBackgroundLight,
    onTertiary = BackgroundPhotoLight
)

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    primary = AccentDark,
    onPrimary = TextPrimaryDark,

    surface = TextPrimaryDark,
    onSurface = TextSecondaryDark,

    secondary = TextFontSecondaryDarkAccent,
    onSecondary = TextTertiaryDark,

    error = ErrorDark,

    tertiary = PanelBackgroundDark,
    onTertiary = BackgroundPhotoDark

)

private val ColoredColorScheme = lightColorScheme(
    background = BackgroundColored,
    onBackground = TextPrimaryColored,

    primary = AccentColored,
    onPrimary = TextPrimaryColored,

    surface = TextPrimaryColored,
    onSurface = TextSecondaryColored,

    secondary = TextFontSecondaryColoredAccent,
    onSecondary = TextTertiaryColored,

    error = ErrorColored,

    tertiary = PanelBackgroundColored,
    onTertiary = BackgroundPhotoColored
)

@Composable
fun MyApplicationTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val colorScheme = when (theme) {
        AppTheme.COLORED -> ColoredColorScheme
        AppTheme.LIGHT -> LightColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.SYSTEM -> if (systemDark) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

val ColorScheme.socialIcon: Color
    @Composable get() = when {
        this === LightColorScheme -> AccentLight
        this === DarkColorScheme -> AccentDark
        else -> AccentColored
    }