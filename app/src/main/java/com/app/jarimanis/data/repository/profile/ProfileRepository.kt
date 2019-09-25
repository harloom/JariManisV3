package com.app.jarimanis.data.repository.profile

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.kategori.ResultKategori
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.models.profile.Profile
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class ProfileRepository (private val api: UserAPI) {
    var job : CompletableJob? = null

    fun getProfile(id: String) : LiveData<Result>{
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