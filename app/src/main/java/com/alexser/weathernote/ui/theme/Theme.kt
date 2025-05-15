import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alexser.weathernote.ui.theme.AccentGrey
import com.alexser.weathernote.ui.theme.DarkGrey
import com.alexser.weathernote.ui.theme.MidGrey
import com.alexser.weathernote.ui.theme.PrimaryGrey

private val WeatherNoteDarkColorScheme = darkColorScheme(
    primary = PrimaryGrey,
    secondary = AccentGrey,
    background = DarkGrey,
    surface = MidGrey,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun WeathernoteTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WeatherNoteDarkColorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
