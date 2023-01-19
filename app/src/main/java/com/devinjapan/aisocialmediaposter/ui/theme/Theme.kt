package com.devinjapan.aisocialmediaposter.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.onDarkHigh
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.onLightHigh
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.primary
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.primaryDark
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.secondary
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.secondaryDark
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.surface
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.surfaceDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = primaryDark,
    secondary = secondaryDark,
    surface = surfaceDark,
    onPrimary = onDarkHigh,
    onSecondary = onLightHigh,
    onBackground = onDarkHigh,
    onSurface = onDarkHigh,
)

private val LightColorPalette = lightColors(
    primary = primary,
    secondary = secondary,
    surface = surface,
    onPrimary = onDarkHigh,
    onSecondary = onDarkHigh,
    onBackground = onLightHigh,
    onSurface = onLightHigh,
)

@Composable
fun AISocialMediaPosterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val useDarkIcons = !isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = if (!darkTheme) surface else surfaceDark,
            darkIcons = useDarkIcons
        )

        onDispose {}
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
