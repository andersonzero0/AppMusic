// Kotlin
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ProgressIndicatorDefaults.drawStopIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DraggableProgressIndicator(
    modifier: Modifier = Modifier,
    activeBall: Boolean = true,
    progress: Float = 0.0f,
    onProgressChange: (Float) -> Unit = {},
) {
    val containerWidth = remember { mutableIntStateOf(0) }
    val internalProgress = remember { mutableFloatStateOf(progress) }
    val ballSize = 20.dp
    val density = LocalDensity.current

    var activeChange by remember { mutableStateOf(true) }

    LaunchedEffect(progress) {
        if (internalProgress.floatValue != progress && activeChange) {
            internalProgress.floatValue = progress
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .onSizeChanged { containerWidth.intValue = it.width }
            .pointerInput(Unit) {
                if (activeBall) detectTapGestures { tapOffset ->
                    if (containerWidth.intValue > 0) {
                        val newProgress = (tapOffset.x / containerWidth.intValue).coerceIn(0f, 1f)
                        onProgressChange(newProgress)
                    }
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        LinearProgressIndicator(
            progress = { internalProgress.floatValue },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
            drawStopIndicator = {
                drawStopIndicator(
                    drawScope = this,
                    stopSize = 0.dp,
                    color = Color.Transparent,
                    strokeCap = StrokeCap.Square
                )
            },
            gapSize = 0.dp,
        )

        if (activeBall) {
            Box(
                modifier = Modifier
                    .offset {
                        val ballWidthPx = with(density) { ballSize.toPx() }
                        val maxOffset = containerWidth.intValue - ballWidthPx
                        val xPos = (maxOffset * internalProgress.floatValue).roundToInt()
                        IntOffset(xPos, 0) // Center vertically
                    }
                    .size(ballSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                activeChange = false
                                val newX = (internalProgress.floatValue * containerWidth.intValue) + dragAmount.x
                                internalProgress.floatValue = (newX / containerWidth.intValue)
                                    .coerceIn(0f, 1f)
                                change.consume()
                            },
                            onDragEnd = {
                                activeChange = true
                                onProgressChange(internalProgress.floatValue)
                            }
                        )
                    }
            )
        }
    }
}

@Preview
@Composable
fun DraggableProgressIndicatorPreview() {
    DraggableProgressIndicator()
}