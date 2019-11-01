package com.app.jarimanis.data.repository.pemberitahuan

import androidx.lifecycle.LiveData
import com.app.jarimanis.data.datasource.models.diskusi.ResponPostComentar
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.diskusi.paging.ResponComentarPaging
import com.app.jarimanis.data.datasource.models.pemberitahuan.Pemberitahuan

import retrofit2.Response

interface PemberitahuanRepository {

    /* not use*/
//    fun getLivepaging(subId : String ,page: String) : LiveData<ResponComentarPaging>
    suspend fun getPaging(page: String) : Response<Pemberitahuan>
    suspend fun deletePemberitahuan(docId: String) : Response<*>


    fun cancelJobs()
}