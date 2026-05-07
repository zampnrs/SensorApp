package br.zampnrs.sensorapp.data.mqtt

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.util.function.Consumer

interface MqttRepository {

    fun connect(callback: MqttConnectionListener)

    fun subscribe(
        topic: String,
        qos: MqttQos = MqttQos.AT_LEAST_ONCE,
        callback: Consumer<Mqtt5Publish>
    )

    fun publish(
        topic: String,
        message: String,
        qos: MqttQos = MqttQos.AT_LEAST_ONCE
    )
}