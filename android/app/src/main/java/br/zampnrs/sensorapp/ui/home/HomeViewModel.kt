package br.zampnrs.sensorapp.ui.home

import androidx.lifecycle.ViewModel
import br.zampnrs.sensorapp.data.mqtt.MqttConstants
import br.zampnrs.sensorapp.data.mqtt.MqttRepository
import br.zampnrs.sensorapp.data.mqtt.MqttConnectionListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mqttRepository: MqttRepository
): ViewModel() {

    internal val _state = MutableStateFlow(HomeContract.State())
    val state = _state.asStateFlow()

    internal val _viewEffect = MutableSharedFlow<HomeContract.ViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    fun intent(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.StartConnection -> {
                connectToServer()
            }

            is HomeContract.Intent.RotateServo -> {
                rotateServo(intent.angle)
            }
        }
    }

    private fun connectToServer() {
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

    private fun subscribeToTopics() {
        subscribeTemperature()
        subscribeHumidity()
        subscribeHeatIndex()
    }

    private fun rotateServo(rotationAngle: Int) {
        if (rotationAngle != state.value.servoMotor.rotationAngle) {
            _state.update {
                it.copy(
                    servoMotor = state.value.servoMotor.copy(
                        rotationAngle = rotationAngle
                    )
                )
            }
            publishServoRotation()
        }
    }

    private fun publishServoRotation() {
        mqttRepository.publish(
            topic = MqttConstants.SERVO_TOPIC,
            message = state.value.servoMotor.toString()
        )
    }

    private fun subscribeTemperature() {
        mqttRepository.subscribe(
            topic = MqttConstants.TEMPERATURE_TOPIC
        ) { payload ->
            _state.update {
                it.copy(
                    dht11 = state.value.dht11.copy(
                        temperature = String(payload.payloadAsBytes).toFloat()
                    )
                )
            }
        }
    }

    private fun subscribeHumidity() {
        mqttRepository.subscribe(
            MqttConstants.HUMIDITY_TOPIC,
        ) { payload ->
            _state.update {
                it.copy(
                    dht11 = state.value.dht11.copy(
                        humidity = String(payload.payloadAsBytes).toFloat()
                    )
                )
            }
        }
    }

    private fun subscribeHeatIndex() {
        mqttRepository.subscribe(
            MqttConstants.HEAT_INDEX_TOPIC
        ) { payload ->
            _state.update {
                it.copy(
                    dht11 = state.value.dht11.copy(
                        heatIndex = String(payload.payloadAsBytes).toFloat()
                    )
                )
            }
        }
    }
}