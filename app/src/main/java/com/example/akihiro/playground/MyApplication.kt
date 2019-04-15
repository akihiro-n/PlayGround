package com.example.akihiro.playground

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    companion object {
        lateinit var getApplication: MyApplication
            private set
    }

    init {
        getApplication = this@MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(clientModule)
        }
    }

}