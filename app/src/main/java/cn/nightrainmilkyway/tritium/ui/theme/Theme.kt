package cn.nightrainmilkyway.tritium.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cn.nightrainmilkyway.tritium.R

val customFontFamily = FontFamily(
    Font(R.font.yuanshen, FontWeight.Normal)
)

val customTypography = Typography(
    headlineSmall = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = customFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    )
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun TritiumTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customNavigationBarColor = Color(0xFFf6eae7)

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = view.context as Activity
            activity.window.navigationBarColor = customNavigationBarColor.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightNavigationBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography,
        content = content
    )
}