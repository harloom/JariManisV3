package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.SentEditThreads

import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import retrofit2.Response

interface ThreadRepository {

    /* not use*/
    fun getLivepaging(subId : String ,page: String) : LiveData<Threads>
    suspend fun getPaging(subId : String, page: String) : Response<Threads>
    suspend fun postThread(upload : UploadThread): Response<UploadThread>


    suspend fun deleteThread(docId: String?) : Response<*>
    suspend fun updateThread(value : SentEditThreads) : Response<*>

    suspend fun getThreadUserPaging(uid : String , page : String) : Response<Threads>
    fun cancelJobs()
}