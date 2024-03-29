package com.devinjapan.aisocialmediaposter.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.devinjapan.aisocialmediaposter.R

@OptIn(ExperimentalTextApi::class)
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// GoogleFont.Provider initialization ...

@OptIn(ExperimentalTextApi::class)
val poppinsFontName = GoogleFont("Poppins")

@OptIn(ExperimentalTextApi::class)
val poppins = FontFamily(Font(googleFont = poppinsFontName, fontProvider = provider))

// Set of Material typography styles to start with
val Typography = Typography(
    defaultFontFamily = poppins,
    body1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    caption = TextStyle(
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    /* Other default text styles to override


    */
)
