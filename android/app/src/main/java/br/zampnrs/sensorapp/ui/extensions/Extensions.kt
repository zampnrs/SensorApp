package br.zampnrs.sensorapp.ui.extensions

import androidx.compose.ui.geometry.Offset
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

fun Offset.getAngle(toOffset: Offset): Double {
    val radiansAngle = atan2(this.y - toOffset.y, this.x - toOffset.x)
    return Math.toDegrees(radiansAngle.toDouble())
}

fun Offset.getDistance(toOffset: Offset): Float {
    return sqrt((this.x - toOffset.x).pow(2) + (this.y - toOffset.y).pow(2))
}

fun Float.toDegrees(): Int {
    return (this*360).toInt()
}