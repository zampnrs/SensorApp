package br.zampnrs.sensorapp.data.di

import br.zampnrs.sensorapp.data.mqtt.MqttRepository
import br.zampnrs.sensorapp.data.mqtt.MqttRepositoryImpl
import br.zampnrs.sensorapp.ui.home.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object AppDI {
    val appModule = module {
        viewModelOf(::HomeViewModel)
        singleOf<MqttClient>(::MqttClientImpl)
        singleOf<MqttRepository, MqttClient>(::MqttRepositoryImpl)
    }
}