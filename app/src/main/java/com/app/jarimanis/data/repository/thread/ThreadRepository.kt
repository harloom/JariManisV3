package com.app.jarimanis.data.repository.thread

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.thread.Threads
import retrofit2.Response

interface ThreadRepository {
    fun getLivepaging(subId : String ,page: String) : LiveData<Threads>
    suspend fun getPaging(subId : String, page: String) : Response<Threads>
    fun cancelJobs()
}