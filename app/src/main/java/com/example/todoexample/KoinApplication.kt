package com.example.todoexample

import android.app.Application
import com.example.todoexample.base.di.DIModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            modules(DIModule)
        }
    }
}
