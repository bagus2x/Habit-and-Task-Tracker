package bagus2x.myhabit.presentation.common.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progressProvider: () -> Float,
    color: Color = MaterialTheme.colors.primary,
    placeholderColor: Color = color.copy(0.1F)
) {
    BoxWithConstraints(modifier = modifier.aspectRatio(1F)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawIndicator(
                color = placeholderColor,
                sweepAngleProvider = { 360F }
            )
            drawIndicator(
                color = color,
                sweepAngleProvider = { progressProvider() * 360 }
            )
        }
    }
}

private fun DrawScope.drawIndicator(
    color: Color,
    sweepAngleProvider: () -> Float,
    width: Float = 12.dp.toPx(),
    style: DrawStyle = Stroke(width = width, cap = StrokeCap.Round)
) {
    drawArc(
        color = color,
        style = style,
        useCenter = false,
        startAngle = -90F,
        sweepAngle = sweepAngleProvider()
    )
}
