package com.lahsuak.apps.geminiai

import android.app.Application
import com.lahsuak.apps.geminiai.di.appModule
import com.lahsuak.apps.geminiai.di.databaseModule
import com.lahsuak.apps.geminiai.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GeminiAIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@GeminiAIApp)
            // Load modules
            modules(appModule, viewModelModule, databaseModule)
        }
    }
}