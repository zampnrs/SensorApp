package br.zampnrs.sensorapp

import android.app.Application
import br.zampnrs.sensorapp.data.di.AppDI
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(AppDI.appModule)
        }
    }
}