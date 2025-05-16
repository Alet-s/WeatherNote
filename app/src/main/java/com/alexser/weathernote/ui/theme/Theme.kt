import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alexser.weathernote.ui.theme.CloudGray
import com.alexser.weathernote.ui.theme.DeepBlue
import com.alexser.weathernote.ui.theme.MistGray
import com.alexser.weathernote.ui.theme.OnDark
import com.alexser.weathernote.ui.theme.OnLight
import com.alexser.weathernote.ui.theme.SkyBlue
import com.alexser.weathernote.ui.theme.SlateBlue


private val WeatherNoteLightColorScheme = lightColorScheme(
    primary = DeepBlue,
    onPrimary = OnDark,
    primaryContainer = SlateBlue,
    onPrimaryContainer = OnDark,

    secondary = SlateBlue,
    onSecondary = OnDark,
    secondaryContainer = SkyBlue,
    onSecondaryContainer = OnDark,

    background = MistGray,
    onBackground = OnLight,

    surface = CloudGray,
    onSurface = OnLight,
    surfaceVariant = SkyBlue,
    onSurfaceVariant = OnLight,

    error = Color(0xFFB00020),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    outline = SlateBlue,
    outlineVariant = MistGray,

    inverseSurface = OnLight,
    inverseOnSurface = OnDark,

    scrim = CloudGray
)




@Composable
fun WeathernoteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WeatherNoteLightColorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
