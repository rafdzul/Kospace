package com.example.kospace.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = SurfaceLightBlue,
    onPrimaryContainer = PrimaryBlue,
    secondary = TextSecondary,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceLightBlue,
    onSurfaceVariant = TextSecondary,
    outline = CardBorderDivider,
    error = StatusRed,
    errorContainer = StatusRedContainer,
    onError = androidx.compose.ui.graphics.Color.White,
    onErrorContainer = StatusRed
)

@Composable
fun KospaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
