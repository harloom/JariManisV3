package com.app.jarimanis.data.repository.firebase

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import retrofit2.Response

interface UserRepository {
    suspend fun getUser(uid : String) : Response<UserRegister>?
    suspend fun saveUser(userRegister: UserRegister): Response<ResponseRegister>
}