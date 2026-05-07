package br.zampnrs.sensorapp.ui.home

sealed class HomeIntent {
    object StartConnection : HomeIntent()
    data class RotateServo(val angle: Int) : HomeIntent()
}