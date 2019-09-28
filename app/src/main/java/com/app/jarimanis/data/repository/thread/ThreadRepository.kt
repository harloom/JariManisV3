package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.ResponServer
import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import retrofit2.Response

interface ThreadRepository {
    fun getLivepaging(subId : String ,page: String) : LiveData<Threads>
    suspend fun getPaging(subId : String, page: String) : Response<Threads>
    suspend fun postThread(upload : UploadThread): Response<UploadThread>
    fun cancelJobs()
}