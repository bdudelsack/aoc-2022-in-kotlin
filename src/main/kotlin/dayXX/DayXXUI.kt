package dayXX

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import readInput


fun main() = application {
    val input = readInput("dayXX/input")

    Window(onCloseRequest = ::exitApplication, title = "DayXX", state = rememberWindowState(width = 800.dp, height = 600.dp)) {
        Canvas(Modifier.fillMaxSize()) {

        }
    }
}
