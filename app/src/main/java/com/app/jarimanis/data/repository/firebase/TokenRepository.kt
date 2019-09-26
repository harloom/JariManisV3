package com.app.jarimanis.data.repository.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.models.profile.Profile
import com.app.jarimanis.data.datasource.models.profile.Result
import com.app.jarimanis.data.datasource.models.token.get_token
import com.app.jarimanis.data.datasource.models.token.sent_token
import com.app.jarimanis.utils.DebugKey
import kotlinx.coroutines.*
import retrofit2.Response

class TokenRepository(val api : UserAPI){
    var job : CompletableJob? = null

    fun getToken(id: sent_token) : LiveData<Response<get_token>> {
        job = Job()
        return  object : LiveData<Response<get_token>>(){
            override fun onActive() {
                super.onActive()
                job?.let { jb->
                    CoroutineScope(Dispatchers.IO + jb).launch {
                        try {
                            val respon = api.getToken(id)
                            withContext(Dispatchers.Main){
                                value = respon

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