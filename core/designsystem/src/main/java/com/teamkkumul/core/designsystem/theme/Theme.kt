package com.teamkkumul.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = MainColor,
    background = White0,
)

private val LocalKkumulTypography = staticCompositionLocalOf<KkumulTypography> {
    error("No KkumulTypography provided")
}

object KkumulTheme {
    val typography: KkumulTypography
        @Composable get() = LocalKkumulTypography.current
}

@Composable
fun ProvideKkumulTypography(typography: KkumulTypography, content: @Composable () -> Unit) {
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)
    CompositionLocalProvider(
        LocalKkumulTypography provides provideTypography,
        content = content,
    )
}

@Composable
fun KkumulAndroidTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    val colorScheme = LightColorScheme
    val typography = kkumulTypography()

    ProvideKkumulTypography(typography) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
