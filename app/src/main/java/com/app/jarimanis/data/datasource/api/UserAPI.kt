package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.models.profile.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPI {
//    @GET("/users")
//    suspend fun getUser

    @POST("users")
    suspend fun saveUser(@Body userRegister: UserRegister): Response<ResponseRegister>

    @GET("users")
    suspend fun getUsers( @Query("profile") id : String?) : Response<Profile>

    @GET("users/mu")
    suspend fun getProfile( @Query("mu") id : String?) : Response<Profile>

}
