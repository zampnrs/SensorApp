package br.zampnrs.sensorapp.ui.home

import br.zampnrs.sensorapp.data.model.DHT11
import br.zampnrs.sensorapp.data.model.ServoMotor

interface HomeContract {
    data class State(
        val dht11: DHT11 = DHT11(),
        val servoMotor: ServoMotor = ServoMotor()
    )

    sealed class Intent {
        object StartConnection : Intent()
        data class RotateServo(val angle: Int) : Intent()
    }

    sealed class ViewEffect
}