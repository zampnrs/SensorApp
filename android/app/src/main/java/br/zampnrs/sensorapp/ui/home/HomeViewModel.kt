package br.zampnrs.sensorapp.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.zampnrs.sensorapp.data.mqtt.MqttConstants
import br.zampnrs.sensorapp.data.mqtt.MqttRepository
import br.zampnrs.sensorapp.data.mqtt.MqttConnectionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mqttRepository: MqttRepository,
): ViewModel() {

    val temperature = mutableStateOf(0f)
    val humidity = mutableStateOf(0f)
    val heatIndex = mutableStateOf(0f)

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

    fun publishServoAngle(angleValue: Int) {
        mqttRepository.publish(
            topic = MqttConstants.SERVO_TOPIC,
            message=angleValue.toString()
        )
    }

    private fun subscribeTemperature() {
        mqttRepository.subscribe(
            topic = MqttConstants.TEMPERATURE_TOPIC
        ) { payload ->
            temperature.value = String(payload.payloadAsBytes).toFloat()
        }
    }

    private fun subscribeHumidity() {
        mqttRepository.subscribe(
            MqttConstants.HUMIDITY_TOPIC,
        ) { payload ->
            humidity.value = String(payload.payloadAsBytes).toFloat()
        }
    }

    private fun subscribeHeatIndex() {
        mqttRepository.subscribe(
            MqttConstants.HEAT_INDEX_TOPIC
        ) { payload ->
            heatIndex.value = String(payload.payloadAsBytes).toFloat()
        }
    }
}