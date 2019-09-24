package com.app.jarimanis

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService

import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.di.NetworkModule
import com.app.jarimanis.di.appModule

import com.app.jarimanis.utils.NotificationID.createChannel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        TokenUser.init(this@App)
        createChannel(this@App)

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOf(NetworkModule, appModule))
        }
    }



}