package br.zampnrs.sensorapp.data.di

import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient

fun interface MqttClient {
    fun provide(): Mqtt5AsyncClient
}