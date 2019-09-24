package com.app.jarimanis.data.repository.firebase

import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.api.UserAPI
import retrofit2.Response

class UserRepositoryImp(private val userAPI: UserAPI)  : UserRepository {
    override suspend fun getUser(uid: String): Response<UserRegister>? {
    return  null
    }

    override suspend fun saveUser(userRegister: UserRegister): Response<ResponseRegister> {
        return userAPI.saveUser(userRegister)
    }

}

