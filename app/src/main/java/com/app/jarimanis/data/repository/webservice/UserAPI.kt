package com.app.jarimanis.data.repository.webservice

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.models.token.get_token
import com.app.jarimanis.data.datasource.models.token.sent_token
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserAPI {
//    @GET("/users")
//    suspend fun getUser

    @POST("users")
    suspend fun saveUser(@Body userRegister: UserRegister): Response<ResponseRegister>

    @POST("user/token")
    suspend fun getToken(@Body data : sent_token ) : Response<get_token>

}
