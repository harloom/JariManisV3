package com.app.jarimanis.di

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.app.jarimanis.LoginActivity
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.local.TokenUser
import com.app.jarimanis.data.service.ServiceInterceptor
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
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
        .authenticator { route, response ->
            if(response.code() ==401 ){
                val intent = Intent(get(),LoginActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                val activity  :  Context = get()
                TokenUser.jwt = null
                FirebaseAuth.getInstance().signOut()
                activity.startActivity(intent)





            }
             null
        }
        .addInterceptor(get())
        .addInterceptor(ServiceInterceptor())
        .connectTimeout(240, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()
      }

    single {
        Retrofit.Builder()
            .baseUrl("https://jarimanisbackend.herokuapp.com/api/v1/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

        factory{ get<Retrofit>().create(JariManisAPI::class.java) }
        factory { get<Retrofit>().create(UserAPI::class.java) }


 }













