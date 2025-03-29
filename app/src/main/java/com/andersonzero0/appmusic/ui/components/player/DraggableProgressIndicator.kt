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
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DraggableProgressIndicator(
    modifier: Modifier = Modifier,
    activeBall: Boolean = true,
) {
    val progress = remember { mutableStateOf(0.5f) }
    val containerWidth = remember { mutableStateOf(0) }
    val ballSize = 20.dp

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .onSizeChanged { containerWidth.value = it.width }
            .pointerInput(Unit) {
                detectTapGestures { tapOffset: Offset ->
                    progress.value = (tapOffset.x / containerWidth.value).coerceIn(0f, 1f)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {

        LinearProgressIndicator(
            progress = { progress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        if (activeBall) {
            Box(
                modifier = Modifier
                    .offset {
                        val xPos =
                            ((containerWidth.value - ballSize.toPx()) * progress.value).roundToInt()
                        IntOffset(xPos, (-ballSize.toPx() / 15).roundToInt())
                    }
                    .size(ballSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            val newX = (progress.value * containerWidth.value) + dragAmount.x
                            progress.value = (newX / containerWidth.value).coerceIn(0f, 1f)
                            change.consume()
                        }
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