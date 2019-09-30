package com.app.jarimanis.data.repository.chat

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.models.ResponseRegister
import com.app.jarimanis.data.datasource.models.UserRegister
import com.app.jarimanis.data.datasource.api.UserAPI
import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.chats.Result
import com.app.jarimanis.data.repository.firebase.UserRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response

class ChatRepositoryImp(private val api: JariManisAPI)  : ChatRepository {
    var job : CompletableJob? = null
    override  fun getChatChannels(): LiveData<List<Result?>>? {
        job = Job()
        return  object : LiveData<List<Result?>>(){
            override fun onActive() {
                super.onActive()
                job?.let { jb->
                    CoroutineScope(IO+ jb).launch {
                        try {
                            val respon = api.getChannelUser()
                            withContext(Main){
                                if(respon.isSuccessful){
                                    val result : Chats? = respon.body()
                                    value = result?.result
                                }
                            }
                        }catch (e : Exception){

                        }
                    }

                }
            }
        }
    }

    override fun cancelJobs() {
            job?.cancel()
    }


}

