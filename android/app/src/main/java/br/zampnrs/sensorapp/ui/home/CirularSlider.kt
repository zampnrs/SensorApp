package br.zampnrs.sensorapp.ui.home

import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import br.zampnrs.sensorapp.ui.extensions.getAngle
import br.zampnrs.sensorapp.ui.extensions.getDistance
import kotlin.math.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CircularSlider(
    modifier: Modifier = Modifier,
    padding: Float = 50f,
    stroke: Float = 25f,
    cap: StrokeCap = StrokeCap.Round,
    touchStroke: Float = 50f,
    thumbColor: Color = Color.Blue,
    progressColor: Color = Color.Black,
    backgroundColor: Color = Color.LightGray,
    onChange: ((Float)->Unit)? = null
) {
    var angle by remember { mutableStateOf(-60f) }
    var lastAngle by remember { mutableStateOf(0f) }
    var appliedAngle by remember { mutableStateOf(0f) }

    var down  by remember { mutableStateOf(false) }
    var radius by remember { mutableStateOf(0f) }
    var center by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(key1 = angle){
        var currentAngle = angle

        if(currentAngle <= 0f){
            currentAngle += 360
        }

        currentAngle = currentAngle.coerceIn(0f, 180f)

        if(lastAngle < 90f && currentAngle == 180f) {
            currentAngle = 0f
        }

        lastAngle = currentAngle
        appliedAngle = currentAngle
    }

    LaunchedEffect(key1 = appliedAngle) {
        onChange?.invoke(appliedAngle/360f)
    }

    Canvas(
        modifier = modifier.then(
            Modifier.onGloballyPositioned {
                center = Offset(x=(it.size.width/2f), y=(it.size.height/2f))
                radius = min(it.size.width.toFloat(), it.size.height.toFloat()) / 2f - padding - stroke/2f
            }.pointerInteropFilter {
                val offset = Offset(it.x, it.y)

                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val dragDistance = offset.getDistance(center)
                        val dragAngle = center.getAngle(offset)

                        if (dragDistance >= radius - touchStroke / 2f && dragDistance <= radius + touchStroke / 2f && dragAngle !in -120f..-60f) {
                            down = true
                            angle = dragAngle.toFloat()
                        } else {
                            down = false
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if (down) {
                            angle = center.getAngle(offset).toFloat()
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        down = false
                    }
                    else -> return@pointerInteropFilter false
                }
                return@pointerInteropFilter true
            }
        )
    ) {
        drawArc(
            color = backgroundColor,
            startAngle = -180f,
            sweepAngle = 180f,
            topLeft = center - Offset(radius, radius),
            size = Size(width = radius*2,height = radius*2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
        drawArc(
            color = progressColor,
            startAngle = 180f,
            sweepAngle = appliedAngle,
            topLeft = center - Offset(radius, radius),
            size = Size(width = radius*2, height = radius*2),
            useCenter = false,
            style = Stroke(
                width = stroke,
                cap = cap
            )
        )
        drawCircle(
            color = thumbColor,
            radius = stroke,
            center = center + Offset(
                radius*cos((180+appliedAngle)*PI/180f).toFloat(),
                radius*sin((180+appliedAngle)*PI/180f).toFloat()
            )
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CircularSliderStandardPreview() {
    CircularSlider(modifier = Modifier.fillMaxSize())
}
