package com.app.jarimanis

import android.app.Application
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.di.NetworkModule
import com.app.jarimanis.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        TokenUser.init(this@App)


        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(NetworkModule, appModule))
        }
    }
}