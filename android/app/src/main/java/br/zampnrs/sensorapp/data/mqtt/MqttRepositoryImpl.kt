package br.zampnrs.sensorapp.data.mqtt

import br.zampnrs.sensorapp.data.di.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.util.function.Consumer

class MqttRepositoryImpl(
    private val client: MqttClient
): MqttRepository {

    override fun connect(callback: MqttConnectionListener) {
        client.provide().connect()
            .whenComplete { _, throwable ->
                if (throwable == null) {
                    callback.success()
                } else {
                    callback.failure(throwable)
                }
            }
    }

    override fun subscribe(
        topic: String,
        qos: MqttQos,
        callback: Consumer<Mqtt5Publish>
    ) {
        client.provide().subscribeWith()
            .topicFilter(topic)
            .qos(qos)
            .callback(callback)
            .send();
    }

    override fun publish(
        topic: String,
        message: String,
        qos: MqttQos
    ) {
        runCatching {
            val publish: Mqtt5Publish = Mqtt5Publish.builder()
                .topic(topic)
                .payload(message.toByteArray())
                .qos(qos)
                .build()
            client.provide().publish(publish)
        }.onFailure {
            it.printStackTrace()
        }
    }
}