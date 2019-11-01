package com.app.jarimanis.data.repository.pemberitahuan

import android.util.Log
import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.api.JariManisAPI
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.diskusi.ResponPostComentar
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.diskusi.paging.ResponComentarPaging
import com.app.jarimanis.data.datasource.models.pemberitahuan.Pemberitahuan

import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.Response
import java.net.SocketTimeoutException


class PemberitahuanRepositoryImp (private  val api : JariManisAPI)  : PemberitahuanRepository {
    override suspend fun deletePemberitahuan(docId: String): Response<*> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override suspend fun getPaging(page: String): Response<Pemberitahuan> {
        return  api.getPemberitahuan(page)
    }



    var job: CompletableJob? = null



    override fun cancelJobs(){
        job?.cancel()
    }

}