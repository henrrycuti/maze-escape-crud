package com.example.mazeescape

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color

// Tema oscuro de Maze Escape (el mismo definido en el laboratorio 6)
private val MazeColors = darkColorScheme(
    primary = Color(0xFFE53935),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF7A0C0C),
    secondary = Color(0xFFFFC107),
    tertiary = Color(0xFF4FA3FF),
    background = Color(0xFF0B0908),
    onBackground = Color(0xFFE8E1DC),
    surface = Color(0xFF14110F),
    onSurface = Color(0xFFE8E1DC),
    surfaceVariant = Color(0xFF231E1B),
    onSurfaceVariant = Color(0xFFE8E1DC),
    outline = Color(0xFF6E625B),
    error = Color(0xFFFF5449)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = MazeColors) {
                PantallaMonstruos()
            }
        }
    }
}
