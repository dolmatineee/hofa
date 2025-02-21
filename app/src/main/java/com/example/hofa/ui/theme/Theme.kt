package com.example.hofa.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF37C8C2), // Новый основной цвет
    onPrimary = Color(0xFF003736), // Цвет текста на основном цвете
    primaryContainer = Color(0xFF00504D), // Контейнер с основным цветом
    onPrimaryContainer = Color(0xFFA0F0EB), // Цвет текста на контейнере с основным цветом
    secondary = Color(0xFFB2CCC9), // Вторичный цвет (сочетается с основным)
    onSecondary = Color(0xFF1C3533), // Цвет текста на вторичном цвете
    secondaryContainer = Color(0xFF393939), // Контейнер с вторичным цветом
    onSecondaryContainer = Color(0xFF858585), // Цвет текста на контейнере с вторичным цветом
    tertiary = Color(0xFFA8D1E8), // Третичный цвет (сочетается с основным)
    onTertiary = Color(0xFF0A3445), // Цвет текста на третичном цвете
    tertiaryContainer = Color(0xFF264B5C), // Контейнер с третичным цветом
    onTertiaryContainer = Color(0xFFC4E7FF), // Цвет текста на контейнере с третичным цветом
    error = Color(0xFFFFB4AB), // Цвет ошибки
    errorContainer = Color(0xFF93000A), // Контейнер с цветом ошибки
    onError = Color(0xFF690005), // Цвет текста на цвете ошибки
    onErrorContainer = Color(0xFFFFDAD6), // Цвет текста на контейнере с цветом ошибки
    background = Color(0xFF101010), // Цвет фона
    onBackground = Color(0xFFE0E3E1), // Цвет текста на фоне
    surface = Color(0xFF242424), // Цвет поверхности
    onSurface = Color(0xFFE0E3E1), // Цвет текста на поверхности
    surfaceVariant = Color(0xFF3F4948), // Вариант цвета поверхности
    onSurfaceVariant = Color(0xFFBEC9C7), // Цвет текста на варианте поверхности
    outline = Color(0xFF797979), // Цвет контура
    inverseOnSurface = Color(0xFF1A1C1A), // Инвертированный цвет текста на поверхности
    inverseSurface = Color(0xFFE0E3E1), // Инвертированный цвет поверхности
    inversePrimary = Color(0xFF006A66), // Инвертированный основной цвет
    surfaceTint = Color(0xFF37C8C2), // Цвет оттенка поверхности
    outlineVariant = Color(0xFF3F4948), // Вариант цвета контура
    scrim = Color(0xFF000000) // Цвет затемнения
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF37C8C2), // Новый основной цвет
    onPrimary = Color(0xFF003736), // Цвет текста на основном цвете
    primaryContainer = Color(0xFFA0F0EB), // Контейнер с основным цветом
    onPrimaryContainer = Color(0xFF003736), // Цвет текста на контейнере с основным цветом
    secondary = Color(0xFF4A6361), // Вторичный цвет (сочетается с основным)
    onSecondary = Color(0xFFFFFFFF), // Цвет текста на вторичном цвете
    secondaryContainer = Color(0xFFDCE3EB), // Контейнер с вторичным цветом
    onSecondaryContainer = Color(0xFF858585), // Цвет текста на контейнере с вторичным цветом
    tertiary = Color(0xFF4A637C), // Третичный цвет (сочетается с основным)
    onTertiary = Color(0xFFFFFFFF), // Цвет текста на третичном цвете
    tertiaryContainer = Color(0xFFD0E4FF), // Контейнер с третичным цветом
    onTertiaryContainer = Color(0xFF031E30), // Цвет текста на контейнере с третичным цветом
    error = Color(0xFFBA1A1A), // Цвет ошибки
    errorContainer = Color(0xFFFFDAD6), // Контейнер с цветом ошибки
    onError = Color(0xFFFFFFFF), // Цвет текста на цвете ошибки
    onErrorContainer = Color(0xFF410002), // Цвет текста на контейнере с цветом ошибки
    background = Color(0xFFFFFFFF), // Цвет фона
    onBackground = Color(0xFF171D1C), // Цвет текста на фоне
    surface = Color(0xFFF4F7F5), // Цвет поверхности
    onSurface = Color(0xFF171D1C), // Цвет текста на поверхности
    surfaceVariant = Color(0xFFDAE5E3), // Вариант цвета поверхности
    onSurfaceVariant = Color(0xFF3F4948), // Цвет текста на варианте поверхности
    outline = Color(0xFF797979), // Цвет контура
    inverseOnSurface = Color(0xFFEBEEEC), // Инвертированный цвет текста на поверхности
    inverseSurface = Color(0xFF2B3231), // Инвертированный цвет поверхности
    inversePrimary = Color(0xFF4EDAD4), // Инвертированный основной цвет
    surfaceTint = Color(0xFF37C8C2), // Цвет оттенка поверхности
    outlineVariant = Color(0xFFBEC9C7), // Вариант цвета контура
    scrim = Color(0xFF000000) // Цвет затемнения
)

@Composable
fun HofaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}