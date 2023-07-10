package br.zampnrs.sensorapp.data.di

import br.zampnrs.sensorapp.data.mqtt.MqttConstants
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.UUID

@Module
@InstallIn(SingletonComponent::class)
object MqttModule {

    @Provides
    fun provideMqttClient(): Mqtt5AsyncClient {
        return Mqtt5Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(MqttConstants.SERVER_URI)
            .serverPort(MqttConstants.SERVER_PORT)
            .buildAsync()
    }

}