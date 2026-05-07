package br.zampnrs.sensorapp.data.di

import br.zampnrs.sensorapp.data.mqtt.MqttConstants
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import java.util.UUID

class MqttClientImpl: MqttClient {
    override fun provide(): Mqtt5AsyncClient {
        return Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(MqttConstants.SERVER_URI)
            .serverPort(MqttConstants.SERVER_PORT)
            .buildAsync()
    }
}