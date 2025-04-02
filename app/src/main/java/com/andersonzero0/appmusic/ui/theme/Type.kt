package com.andersonzero0.appmusic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import androidx.compose.ui.text.googlefonts.GoogleFont
import com.andersonzero0.appmusic.R

//val provider = GoogleFont.Provider(
//    providerAuthority = "com.google.android.gms.fonts",
//    providerPackage = "com.google.android.gms",
//    certificates = R.array.com_google_android_gms_fonts_certs
//)
//
//val bodyFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Poppins"),
//        fontProvider = provider,
//    )
//)
//
//val displayFontFamily = FontFamily(
//    Font(
//        googleFont = GoogleFont("Poppins"),
//        fontProvider = provider,
//    )
//)

val defaultFontFamily = FontFamily(
    Font(R.font.author_regular, FontWeight.Normal),
    Font(R.font.author_extralight, FontWeight.Thin),
    Font(R.font.author_extralight, FontWeight.ExtraLight),
    Font(R.font.author_light, FontWeight.Light),
    Font(R.font.author_medium, FontWeight.Medium),
    Font(R.font.author_bold, FontWeight.Bold),
    Font(R.font.author_bold, FontWeight.ExtraBold),
    Font(R.font.author_bold, FontWeight.Black),
    Font(R.font.author_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.author_extralightitalic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.author_extralightitalic, FontWeight.ExtraLight, FontStyle.Italic),
    Font(R.font.author_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.author_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.author_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.author_bolditalic, FontWeight.ExtraBold, FontStyle.Italic),
    Font(R.font.author_bolditalic, FontWeight.Black, FontStyle.Italic),
    Font(R.font.author_variable, FontWeight.Normal),
    Font(R.font.author_variableitalic, FontWeight.Normal, FontStyle.Italic),
)

val displayFontFamily = FontFamily(
    Font(R.font.clashdisplay_regular, FontWeight.Normal),
    Font(R.font.clashdisplay_extralight, FontWeight.ExtraLight),
    Font(R.font.clashdisplay_light, FontWeight.Light),
    Font(R.font.clashdisplay_medium, FontWeight.Medium),
    Font(R.font.clashdisplay_semibold, FontWeight.SemiBold),
    Font(R.font.clashdisplay_bold, FontWeight.Bold),
    Font(R.font.clashdisplay_variable, FontWeight.Normal),
)

// Default Material 3 typography values
val baseline = Typography()

val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = defaultFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = defaultFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = defaultFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = defaultFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = defaultFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = defaultFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = defaultFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = defaultFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = defaultFontFamily),
)

