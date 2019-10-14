package com.app.jarimanis.data.repository.profile

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.models.profile.Profile
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.datasource.models.profile.Sent_change
import com.app.jarimanis.data.datasource.models.token.sent_token
import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response

class ProfileRepositoryImp (private val api: UserAPI) : ProfileRepository {
    override suspend fun ubahFoto(value: String): Response<*> {
        return  api.updateProfile(TYPE_UPDATE_IMG, Sent_change(thumbail = value))
    }

    override suspend fun ubahNumberPhone(value: String): Response<*> {
        return api.updateProfile(TYPE_UPDATE_NUMBERPHONE, Sent_change(numberPhone = value))
    }
    val TYPE_UPDATE_IMG = "img"
    val TYPE_UPDATE_NAME = "name"
    val TYPE_UPDATE_NUMBERPHONE = "numberPhone"
    override suspend fun ubahNama(value: String): Response<*> {
        return api.updateProfile(TYPE_UPDATE_NAME, Sent_change(nameUser = value))
    }

    var job : CompletableJob? = null

    override fun getProfile(id: String) : LiveData<Result>{
        job = Job()
        return  object : LiveData<Result>(){
            override fun onActive() {
                super.onActive()
                job?.let { jb->
                    CoroutineScope(IO+ jb).launch {
                        try {
                            val respon = api.getProfile(id)
                            withContext(Main){
                                if(respon.isSuccessful){
                                    val result : Profile? = respon.body()
                                    value = result?.result
                                }
                            }
                        }catch (e : Exception){
                            Log.d(DebugKey.key, e.toString());
                        }
                    }

                }
            }
        }
    }



    fun cancel(){
        job?.cancel()
    }
}