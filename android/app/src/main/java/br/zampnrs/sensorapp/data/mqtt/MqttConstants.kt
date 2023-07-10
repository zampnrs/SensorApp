package br.zampnrs.sensorapp.data.mqtt

object MqttConstants {
    const val SERVER_URI = "broker.hivemq.com"
    const val SERVER_PORT = 1883
    const val USERNAME = ""
    const val PASSWORD = ""

    private const val BASE_TOPIC = "/SensorApp"
    const val SERVO_TOPIC = "$BASE_TOPIC/servo"
    const val TEMPERATURE_TOPIC = "$BASE_TOPIC/temperature"
    const val HUMIDITY_TOPIC = "$BASE_TOPIC/humidity"
    const val HEAT_INDEX_TOPIC = "$BASE_TOPIC/heatIndex"
}