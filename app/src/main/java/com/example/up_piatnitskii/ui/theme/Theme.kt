package com.example.up_piatnitskii.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,              // Основной цвет бренда
    secondary = RedColor,               // Вторичный/акцентный цвет

    // Фоны
    background = BackgroundColor,
    surface = BlockColor,
    surfaceVariant = BackgroundColor,

    // Текст
    onPrimary = BlockColor,             // Текст на основном цвете
    onSecondary = BlockColor,           // Текст на вторичном цвете
    onBackground = TextColor,           // Основной текст на фоне
    onSurface = TextColor,              // Текст на поверхности

    // Статусные цвета
    error = RedColor,
    onError = BlockColor,

    // Дополнительные
    outline = SubTextLightColor,
    outlineVariant = SubTextDarkColor,

    // Для disabled состояний
    scrim = DisableColor,
)

private val LightColorScheme = lightColorScheme(
    primary = AccentColor,              // Основной цвет бренда
    secondary = RedColor,               // Вторичный/акцентный цвет

    // Фоны
    background = BackgroundColor,
    surface = BlockColor,
    surfaceVariant = BackgroundColor,

    // Текст
    onPrimary = BlockColor,             // Текст на основном цвете
    onSecondary = BlockColor,           // Текст на вторичном цвете
    onBackground = TextColor,           // Основной текст на фоне
    onSurface = TextColor,              // Текст на поверхности

    // Статусные цвета
    error = RedColor,
    onError = BlockColor,

    // Дополнительные
    outline = SubTextLightColor,
    outlineVariant = SubTextDarkColor,

    // Для disabled состояний
    scrim = DisableColor,
)

@Composable
fun UPPiatnitskiiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}