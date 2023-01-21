package com.devinjapan.aisocialmediaposter.ui.theme

import androidx.compose.ui.graphics.Color
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.onDarkMedium
import com.devinjapan.aisocialmediaposter.ui.theme.ThemeColors.onLightMedium

// Color list

private val pink = Color(0xFFE91E63)
private val blue = Color(0xFF0091EA)
private val lightPink = Color(0xFFF8BBD0)

// White
private val white = Color(0xFFFFFFFF)
private val whiteAlpha74 = Color(0xBDFFFFFF)
private val whiteAlpha38 = Color(0x61FFFFFF)
private val whiteAlpha12 = Color(0x146200EE)
private val whiteAlpha8 = Color(0x14FFFFFF)

// Black
private val blackAlpha87 = Color(0xDE000000)
private val blackAlpha60 = Color(0x99000000)
private val blackAlpha38 = Color(0x61000000)
private val blackAlpha8 = Color(0x14000000)

// Grey
private val darkGray = Color(0xFF121212)
private val mediumGray = Color(0x1FFFFFFF)
private val lightGray = Color(0x146200EE)

val reddishBrown = Color(0xFFBF360C)

object ThemeColors {
    // TEXT
// Dark
    val onLightHigh = blackAlpha87
    val onLightMedium = blackAlpha60
    val onLightDisabled = blackAlpha38

    // Light
    val onDarkHigh = white
    val onDarkMedium = whiteAlpha74
    val onDarkDisabled = whiteAlpha38

    val error = reddishBrown

    // SURFACES
// Dark
    val surfaceDark = darkGray
    val primaryDark = blue
    val secondaryDark = lightPink

    // Light
    val surface = white
    val primary = blue
    val secondary = pink
}

object CustomColors {

    val TopBarGray = mediumGray

    val DarkChip = whiteAlpha8
    val LightChip = blackAlpha8

    val DarkChipCloseButton = onDarkMedium
    val LightChipCloseButton = onLightMedium

    val ButtonBorderLight = whiteAlpha12
    val ButtonBorderDark = whiteAlpha12

    val TintSelectedDark = blackAlpha87
    val TintUnselectedDark = whiteAlpha38
    val TintSelectedLight = white
    val TintUnselectedLight = blackAlpha38

    val ButtonBackgroundSelectedDark = white
    val ButtonBackgroundUnselectedDark = Color.Transparent
    val ButtonBackgroundSelectedLight = blackAlpha87
    val ButtonBackgroundUnselectedLight = Color.Transparent
}
