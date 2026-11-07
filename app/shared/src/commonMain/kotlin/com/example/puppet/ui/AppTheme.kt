package com.example.puppet.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF5B3E96),
    surface = Color(0xFFFFFBFE),
    background = Color(0xFFFFFBFE),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFCFBCFF),
    surface = Color(0xFF1C1B1F),
    background = Color(0xFF141218),
)

@Composable
fun AppTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
