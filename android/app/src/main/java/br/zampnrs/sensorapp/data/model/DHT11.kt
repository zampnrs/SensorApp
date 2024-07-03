package br.zampnrs.sensorapp.data.model

data class DHT11(
    val temperature: Float = 0f,
    val humidity: Float = 0f,
    val heatIndex: Float = 0f
)
