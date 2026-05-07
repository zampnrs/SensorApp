package br.zampnrs.sensorapp.ui.home

import br.zampnrs.sensorapp.data.model.DHT11
import br.zampnrs.sensorapp.data.model.ServoMotor

data class HomeState(
    val dht11: DHT11 = DHT11(),
    val servoMotor: ServoMotor = ServoMotor()
)
