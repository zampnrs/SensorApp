package br.zampnrs.sensorapp.data.mqtt

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.util.function.Consumer
import javax.inject.Inject


class MqttRepository @Inject constructor(
    private val client: Mqtt5AsyncClient
) {

    fun connect(callback: MqttConnectionListener) {
        client.connect()
            .whenComplete { _, throwable ->
                if (throwable == null) {
                    callback.success()
                } else {
                    callback.failure(throwable)
                }
            }
    }

    fun subscribe(
        topic: String,
        qos: MqttQos = MqttQos.AT_LEAST_ONCE,
        callback: Consumer<Mqtt5Publish>
    ) {
        client.subscribeWith()
            .topicFilter(topic)
            .qos(qos)
            .callback(callback)
            .send();
    }

    fun publish(
        topic: String,
        message: String,
        qos: MqttQos = MqttQos.AT_LEAST_ONCE
    ) {
        runCatching {
            val publish: Mqtt5Publish = Mqtt5Publish.builder()
                .topic(topic)
                .payload(message.toByteArray())
                .qos(qos)
                .build()
            client.publish(publish)
        }.onFailure {
            it.printStackTrace()
        }
    }
}