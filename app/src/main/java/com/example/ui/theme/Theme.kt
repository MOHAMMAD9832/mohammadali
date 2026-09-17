package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = BrandBluePrimaryDark,
    onPrimary = BrandBlueOnPrimaryDark,
    primaryContainer = BrandBlueContainerDark,
    onPrimaryContainer = BrandBlueOnContainerDark,
    secondary = EmeraldSecondaryDark,
    onSecondary = EmeraldOnSecondaryDark,
    secondaryContainer = EmeraldSecondaryContainerDark,
    onSecondaryContainer = EmeraldOnSecondaryContainerDark,
    tertiary = SaffronTertiaryDark,
    onTertiary = SaffronOnTertiaryDark,
    tertiaryContainer = SaffronTertiaryContainerDark,
    onTertiaryContainer = SaffronOnTertiaryContainerDark,
    background = BackgroundDark,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = OutlineDark,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = BrandBluePrimary,
    onPrimary = BrandBlueOnPrimary,
    primaryContainer = BrandBlueContainer,
    onPrimaryContainer = BrandBlueOnContainer,
    secondary = EmeraldSecondary,
    onSecondary = EmeraldOnSecondary,
    secondaryContainer = EmeraldSecondaryContainer,
    onSecondaryContainer = EmeraldOnSecondaryContainer,
    tertiary = SaffronTertiary,
    onTertiary = SaffronOnTertiary,
    tertiaryContainer = SaffronTertiaryContainer,
    onTertiaryContainer = SaffronOnTertiaryContainer,
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = OutlineLight,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep consistent branding colors
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
