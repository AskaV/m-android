package com.spp.android.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,       // Main background color for the entire screen.
    onBackground = TextPrimaryLight,    // Color of content (text/icons) displayed on the background.

    primary = AccentLight,              // Primary accent color — used for buttons, active elements, toggles, etc.
    onPrimary = TextPrimaryLight,       // Color of text and icons displayed on top of the primary color.

    surface = TextPrimaryLight,         // Surface color — used for cards, sheets, panels, and other containers.
    onSurface = TextSecondaryLight,     // Color of text and icons displayed on top of surfaces.

//    secondary = TextSecondaryLight,     // Secondary accent — typically used for chips, icons, and subtle highlights.
    onSecondary = TextTertiaryLight,    // Color used on top of the secondary color — typically for text inside chips or badges.

    error = ErrorLight,                 // Error color — used for validation messages, error states, and indicators.
    //onError = TextPrimaryLight          // Color of text/icons displayed on top of the error color.
)

private val DarkColorScheme = darkColorScheme(
    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    primary = AccentDark,
    onPrimary = TextPrimaryDark,

    surface = TextPrimaryDark,
    onSurface = TextSecondaryDark,

    onSecondary = TextTertiaryDark,

    error = ErrorDark,
)

private val ColoredColorScheme = lightColorScheme(
    background = BackgroundColored,
    onBackground = TextPrimaryColored,

    primary = AccentColored,
    onPrimary = TextPrimaryColored,

    surface = TextPrimaryColored,
    onSurface = TextSecondaryColored,

    onSecondary = TextTertiaryColored,

    error = ErrorColored,
)

@Composable
fun MyApplicationTheme(
    themePref: String = "system",
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val colorScheme = when (themePref) {
        "colored" -> ColoredColorScheme
        else -> if (systemDark) DarkColorScheme else LightColorScheme
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