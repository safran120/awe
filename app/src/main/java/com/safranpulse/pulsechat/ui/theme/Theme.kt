package com.safranpulse.pulsechat.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    primaryContainer = BrandIndigoContainer,
    onPrimaryContainer = BrandIndigoOnContainer,
    secondary = Color(0xFF64748B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF1F5F9),
    onSecondaryContainer = Color(0xFF1E293B),
    tertiary = Color(0xFF8B5CF6),
    onTertiary = Color.White,
    surface = Color(0xFFF8FAFC),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    background = Color(0xFFFAFBFD),
    onBackground = Color(0xFF0F172A),
    error = ErrorRed,
    onError = Color.White,
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    inverseSurface = Color(0xFF0F172A),
    inverseOnSurface = Color(0xFFF8FAFC),
    inversePrimary = BrandIndigo,
    surfaceDim = Color(0xFFE2E8F0),
    surfaceBright = Color(0xFFFFFFFF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color(0xFFCBD5E1)
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    primaryContainer = BrandIndigoOnContainer,
    onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFF94A3B8),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF334155),
    onSecondaryContainer = Color(0xFFF8FAFC),
    tertiary = Color(0xFFC084FC),
    onTertiary = Color(0xFF1E1B4B),
    surface = SurfaceDim,
    onSurface = OnSurface,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = OnSurfaceVariant,
    background = Color(0xFF0B1120),
    onBackground = OnSurface,
    error = ErrorRed,
    onError = Color(0xFF0F172A),
    outline = DividerLine,
    outlineVariant = SurfaceElevated,
    inverseSurface = OnSurface,
    inverseOnSurface = SurfaceDim,
    inversePrimary = BrandIndigo,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceElevated,
    surfaceContainerLowest = SurfaceDim,
    surfaceContainerLow = SurfaceElevated,
    surfaceContainer = SurfaceRaised,
    surfaceContainerHigh = Color(0xFF475569),
    surfaceContainerHighest = Color(0xFF64748B)
)

@Composable
fun PulseChatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
