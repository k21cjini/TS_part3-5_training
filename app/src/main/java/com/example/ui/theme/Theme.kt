package com.example.ui.theme

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
  primary = PrimaryBoldBlueLight,
  onPrimary = Color(0xFF003258),
  primaryContainer = PrimaryBoldBlueContainerDark,
  onPrimaryContainer = Color(0xFFD1E4FF),
  secondary = SecondarySlateLight,
  onSecondary = Color(0xFF253140),
  secondaryContainer = SecondarySlateContainerDark,
  onSecondaryContainer = Color(0xFFD7E3F7),
  tertiary = TertiaryIndigoLight,
  onTertiary = Color(0xFF3B2948),
  tertiaryContainer = TertiaryIndigoContainerDark,
  onTertiaryContainer = Color(0xFFF2DAFF),
  error = Color(0xFFFFB4AB),
  errorContainer = Color(0xFF93000A),
  onError = Color(0xFF690005),
  onErrorContainer = Color(0xFFFFDAD6),
  background = BgDark,
  onBackground = TextPrimaryDark,
  surface = SurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = TextSecondaryDark,
  outline = OutlineDark,
  outlineVariant = OutlineVariantDark
)

private val LightColorScheme = lightColorScheme(
  primary = PrimaryBoldBlue,
  onPrimary = Color.White,
  primaryContainer = PrimaryBoldBlueContainer,
  onPrimaryContainer = Color(0xFF001D36),
  secondary = SecondarySlate,
  onSecondary = Color.White,
  secondaryContainer = SecondarySlateContainer,
  onSecondaryContainer = Color(0xFF0F1C2B),
  tertiary = TertiaryIndigo,
  onTertiary = Color.White,
  tertiaryContainer = TertiaryIndigoContainer,
  onTertiaryContainer = Color(0xFF251432),
  error = ErrorCrimson,
  errorContainer = ErrorCrimsonContainer,
  onError = Color.White,
  onErrorContainer = Color(0xFF410002),
  background = BgLight,
  onBackground = TextPrimaryLight,
  surface = SurfaceLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = TextSecondaryLight,
  outline = OutlineLight,
  outlineVariant = OutlineVariantLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our handcrafted bold typography palette
  content: @Composable () -> Unit,
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
    typography = Typography,
    content = content
  )
}

