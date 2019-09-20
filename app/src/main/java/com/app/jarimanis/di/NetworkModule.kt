package com.app.jarimanis.di

import com.app.jarimanis.data.repository.webservice.UserAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val NetworkModule = module {

    //    single<OkHttpClient>(DEFAULT_NAMESPACE) { provideDefaultOkhttpClient() }
    single { provideRetrofit(get()) }
    factory { userAPI(get()) }
//    factory { PeoreApiSecond(get()) }
    single { makeOkHttpClient() }


}

//fun provideDefaultOkhttpClient(): OkHttpClient {
//    return OkHttpClient.Builder()
//        .addInterceptor(ApiKeyInterceptor())
//        .build()
//}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("http://10.0.2.2:3000/api/v1/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
private fun makeOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(makeLoggingInterceptor())
        .connectTimeout(240, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()
}

private fun makeLoggingInterceptor(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level =
        HttpLoggingInterceptor.Level.BODY
    return logging
}
fun userAPI(retrofit: Retrofit): UserAPI = retrofit.create(UserAPI::class.java)
//fun jariAPI(retrofit: Retrofit) : ApiPeore = retrofit.create(ApiPeore::class.java)
const val DEFAULT_NAMESPACE = "default"