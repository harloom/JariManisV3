package com.app.jarimanis.data.repository.profile

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.profile.Result
import retrofit2.Response

interface ProfileRepository {

   suspend fun ubahNama(value: String): Response<*>
    suspend  fun ubahNumberPhone(value: String) : Response<*>
    suspend fun ubahFoto(value: String) : Response<*>
    suspend fun ubahDate(value: Long) : Response<*>
    fun getProfile(id: String): LiveData<Result>
}