package com.app.jarimanis.di

import android.util.Log
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.api.UserAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {
    factory<Interceptor> {
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { Log.d("API", it) })
            .setLevel(HttpLoggingInterceptor.Level.HEADERS)
    }

    factory {OkHttpClient.Builder()
        .addInterceptor(get())
        .connectTimeout(240, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()
      }

    single {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/api/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

        factory{ get<Retrofit>().create(JariManisAPI::class.java) }
        factory { get<Retrofit>().create(UserAPI::class.java) }


 }













