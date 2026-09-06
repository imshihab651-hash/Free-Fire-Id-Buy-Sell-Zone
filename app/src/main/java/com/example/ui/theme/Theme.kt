package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = FireOrange,
    onPrimary = GamingDarkBg,
    primaryContainer = FireOrangeDark,
    onPrimaryContainer = TextPrimary,
    secondary = CyberGold,
    onSecondary = GamingDarkBg,
    secondaryContainer = GamingSurfaceElevated,
    onSecondaryContainer = CyberGoldLight,
    tertiary = NeonCyan,
    onTertiary = GamingDarkBg,
    tertiaryContainer = GamingSurfaceVariant,
    onTertiaryContainer = NeonCyan,
    background = GamingDarkBg,
    onBackground = TextPrimary,
    surface = GamingSurface,
    onSurface = TextPrimary,
    surfaceVariant = GamingSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GamingBorder,
    outlineVariant = GamingDivider,
    error = GamingRed,
    onError = TextPrimary,
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content,
  )
}
