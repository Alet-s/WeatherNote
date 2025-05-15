import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.alexser.weathernote.ui.theme.NYTBlack
import com.alexser.weathernote.ui.theme.NYTGray
import com.alexser.weathernote.ui.theme.NYTRed
import com.alexser.weathernote.ui.theme.NYTSoftGray
import com.alexser.weathernote.ui.theme.NYTWhite

private val WeatherNoteLightColorScheme = lightColorScheme(
    primary = NYTRed,
    secondary = NYTGray,
    background = NYTWhite,
    surface = NYTSoftGray,
    onPrimary = Color.White,
    onSecondary = NYTBlack,
    onBackground = NYTBlack,
    onSurface = NYTBlack
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
