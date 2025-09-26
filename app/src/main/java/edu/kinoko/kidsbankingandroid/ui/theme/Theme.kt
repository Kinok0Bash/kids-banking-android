package edu.kinoko.kidsbankingandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,

    secondary = Secondary,
    onSecondary = LilGray,

    background = White,
    onBackground = Black,

    surface = White,
    onSurface = Black
)

@Composable
fun KidsBankingAndroidTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}