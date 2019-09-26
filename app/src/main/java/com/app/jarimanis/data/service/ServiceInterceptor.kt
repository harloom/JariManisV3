package com.app.jarimanis.data.service

import com.app.jarimanis.data.datasource.local.TokenUser
import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor : Interceptor {
    var token : String = ""

    fun Token(token: String ) {
        this.token = token;
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if(request.header("No-Authentication")==null){
            val token = TokenUser.jwt;
            println("intercept : $token")
            if(!token.isNullOrBlank())
            {
                val finalToken = "Bearer $token"
                request = request.newBuilder()
                    .addHeader("Authorization",finalToken)
                    .build()
            }

        }

        return chain.proceed(request)
    }


}
