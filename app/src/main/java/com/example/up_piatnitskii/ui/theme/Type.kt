package com.example.up_piatnitskii.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.example.up_piatnitskii.R

val ralewayFontFamily = FontFamily(
    Font(R.font.raleway, FontWeight.Normal),
    Font(R.font.raleway_medium, FontWeight.Medium),
    Font(R.font.raleway_semibold, FontWeight.SemiBold),
    Font(R.font.raleway_bold, FontWeight.Bold),
)
// Set of Material typography styles to start with
object RalewayTypography {
    // HEADING
    val headingRegular34 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal, // Raleway Regular
        fontSize = 34.sp,
    )

    val headingRegular32 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,

    )

    val headingBold30 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Bold, // Raleway Bold
        fontSize = 30.sp,
    )

    val headingRegular26 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp,
    )

    val headingRegular20 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    )

    val headingSemiBold16 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.SemiBold, // Raleway SemiBold
        fontSize = 16.sp,
    )

    // SUBTITLE
    val subtitleRegular16 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )

    // BODY
    val bodyRegular24 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
    )

    val bodyRegular20 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
    )

    val bodySemiBold18 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
    )

    val bodyMedium16 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Medium, // Raleway Medium
        fontSize = 16.sp,
    )

    val bodyRegular16 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    )

    val bodyMedium14 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
    )

    val bodyRegular14 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )

    val bodyRegular12 = TextStyle(
        fontFamily = ralewayFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    )
}