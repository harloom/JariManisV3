package com.app.jarimanis.data.repository.thread

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.api.JariManisAPI

import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response
import java.net.SocketTimeoutException


class ThreadRepositoryImp (private  val api : JariManisAPI)  : ThreadRepository {
    override suspend fun postThread(upload: UploadThread): Response<UploadThread> {
        return api.postThreads(upload)
    }

    override suspend fun getPaging(subId: String, page: String): Response<Threads> {
        return  api.getThreads(subId,page)
    }

    var job: CompletableJob? = null
    override fun getLivepaging(subId: String, page: String): LiveData<Threads> {
        job = Job()
        return object: LiveData<Threads>(){
            override fun onActive() {
                super.onActive()
                job?.let{ theJob ->
                    CoroutineScope(IO + theJob).launch {
                        try {
                            val respon = api.getThreads(subId,page)
                            withContext(Main){
                                if(respon.isSuccessful){
                                    val threads  : Threads? = respon.body()
                                    value  = threads
                                    theJob.complete()
                                }

                            }
                        }catch (se : SocketTimeoutException){
                            cancelJobs()
                            Log.e("Io Connection", "Error: ${se.message}")
                        }



                    }

                }

            }
        }
    }


    override fun cancelJobs(){
        job?.cancel()
    }

}