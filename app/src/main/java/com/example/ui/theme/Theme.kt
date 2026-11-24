package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = EmeraldVibrant,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1EAD9),
    onPrimaryContainer = EmeraldNight,
    secondary = GoldRoyal,
    onSecondary = ArabianBrownDeep,
    secondaryContainer = GoldSoftContainer,
    onSecondaryContainer = ArabianBrownDeep,
    tertiary = ArabianBrownMedium,
    onTertiary = Color.White,
    tertiaryContainer = ArabianWarmSand,
    onTertiaryContainer = ArabianBrownDeep,
    background = ArabianSandBg,
    onBackground = ArabianBrownDeep,
    surface = ArabianCardSurface,
    onSurface = ArabianBrownDeep,
    surfaceVariant = ArabianWarmSand,
    onSurfaceVariant = ArabianBrownMedium,
    outline = ArabianBrownLight,
    error = StatusError,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldBright,
    onPrimary = EmeraldNight,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = Color(0xFFD7F2E2),
    secondary = GoldRadiant,
    onSecondary = ArabianBrownDeep,
    secondaryContainer = GoldDarkContainer,
    onSecondaryContainer = GoldSoftContainer,
    tertiary = GoldRoyal,
    onTertiary = ArabianBrownDeep,
    background = DarkArabianBg,
    onBackground = DarkOnArabian,
    surface = DarkArabianSurface,
    onSurface = DarkOnArabian,
    surfaceVariant = DarkArabianSurfaceVariant,
    onSurfaceVariant = DarkOnArabianVariant,
    outline = ArabianBrownMedium,
    error = StatusErrorDark,
    onError = Color(0xFF4A0000)
)

val AppShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun IhyaulArabiyyahTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
