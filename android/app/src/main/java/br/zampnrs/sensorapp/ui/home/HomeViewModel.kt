package br.zampnrs.sensorapp.ui.home

import androidx.lifecycle.ViewModel
import br.zampnrs.sensorapp.data.model.DHT11
import br.zampnrs.sensorapp.data.model.ServoMotor
import br.zampnrs.sensorapp.data.mqtt.MqttConstants
import br.zampnrs.sensorapp.data.mqtt.MqttRepository
import br.zampnrs.sensorapp.data.mqtt.MqttConnectionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mqttRepository: MqttRepository,
): ViewModel() {

    internal val _dht11 = MutableStateFlow(DHT11())
    val dht11 = _dht11.asStateFlow()

    internal val _servoMotor = MutableStateFlow(ServoMotor())
    val servoMotor = _servoMotor.asStateFlow()

    fun connectToServer() {
        mqttRepository.connect(
            callback = object : MqttConnectionListener {
                override fun success() {
                    subscribeToTopics()
                }

                override fun failure(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        )
    }

    fun subscribeToTopics() {
        subscribeTemperature()
        subscribeHumidity()
        subscribeHeatIndex()
    }

    fun rotateServo(rotationAngle: Int) {
        if (rotationAngle != _servoMotor.value.rotationAngle) {
            _servoMotor.update { it.copy(rotationAngle = rotationAngle) }
            publishServoRotation()
        }
    }

    private fun publishServoRotation() {
        mqttRepository.publish(
            topic = MqttConstants.SERVO_TOPIC,
            message = _servoMotor.value.toString()
        )
    }

    private fun subscribeTemperature() {
        mqttRepository.subscribe(
            topic = MqttConstants.TEMPERATURE_TOPIC
        ) { payload ->
            _dht11.update {
                it.copy(temperature = String(payload.payloadAsBytes).toFloat())
            }
        }
    }

    private fun subscribeHumidity() {
        mqttRepository.subscribe(
            MqttConstants.HUMIDITY_TOPIC,
        ) { payload ->
            _dht11.update {
                it.copy(humidity = String(payload.payloadAsBytes).toFloat())
            }
        }
    }

    private fun subscribeHeatIndex() {
        mqttRepository.subscribe(
            MqttConstants.HEAT_INDEX_TOPIC
        ) { payload ->
            _dht11.update {
                it.copy(heatIndex = String(payload.payloadAsBytes).toFloat())
            }
        }
    }
}