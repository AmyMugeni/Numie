package com.numie.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = NumiePink,
    secondary = NumieMint,
    tertiary = NumiePurple,
    background = NumieWhite,
    surface = NumieGrayLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = NumieGrayDark,
    onSurface = NumieGrayDark,
)

@Composable
fun NumieTheme(
    darkTheme: Boolean = false, // we can add dark mode later
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
