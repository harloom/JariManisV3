package com.app.jarimanis.data.repository.commentar

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.diskusi.paging.ResponComentarPaging

import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response
import java.net.SocketTimeoutException


class ComentarRepositoryImp (private  val api : JariManisAPI)  : DiskusiRepository {
    override suspend fun getPaging(subId: String, page: String): Response<ResponComentarPaging> {
        return  api.getDiskusi(subId,page)
    }

    override suspend fun deleteDiskusi(docId: String?): Response<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    var job: CompletableJob? = null



    override fun cancelJobs(){
        job?.cancel()
    }

}