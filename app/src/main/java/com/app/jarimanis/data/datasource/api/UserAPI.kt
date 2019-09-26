package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.models.profile.Profile
import com.app.jarimanis.data.datasource.models.token.get_token
import com.app.jarimanis.data.datasource.models.token.sent_token
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {
//    @GET("/users")
//    suspend fun getUser

    @POST("users")
    suspend fun saveUser(@Body userRegister: UserRegister): Response<ResponseRegister>

    @Headers("No-Authentication: true")
    @GET("users")
    suspend fun getUsers( @Query("profile") id : String?) : Response<Profile>

    @GET("users/mu")
    suspend fun getProfile( @Query("mu") id : String?) : Response<Profile>

    @POST("users/token")
    suspend fun getToken(@Body uid : sent_token) : Response<get_token>

}
