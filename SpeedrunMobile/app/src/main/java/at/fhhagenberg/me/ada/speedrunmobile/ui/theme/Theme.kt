package at.fhhagenberg.me.ada.speedrunmobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = BackgroundGrey,
    onPrimary = Color.White,
    primaryVariant = BackgroundGreyLight,
    secondary = HeaderGreen,
    surface = BackgroundBlackLight,
    background = BackgroundBlack,
    onSurface = Color.White,
    onBackground = Color.White
)

private val LightColorPalette = lightColors(
    primary = BackgroundGreyLight,
    onPrimary = Color.White,
    primaryVariant = BackgroundGreyLightLight,
    secondary = HeaderGreen,
    surface = BackgroundBlackLightLight,
    background = BackgroundBlackLight,
    onSurface = Color.White,
    onBackground = Color.White
)

@Composable
fun SpeedrunMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}