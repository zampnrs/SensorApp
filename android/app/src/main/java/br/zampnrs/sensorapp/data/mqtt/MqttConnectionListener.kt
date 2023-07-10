package br.zampnrs.sensorapp.data.mqtt

interface MqttConnectionListener {

    fun success()
    fun failure(throwable: Throwable)

}