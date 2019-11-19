package com.app.jarimanis.data.repository.commentar

import com.app.jarimanis.data.datasource.models.diskusi.ResponPostComentar
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.diskusi.paging.ResponComentarPaging

import retrofit2.Response

interface DiskusiRepository {

    /* not use*/
//    fun getLivepaging(subId : String ,page: String) : LiveData<ResponComentarPaging>
    suspend fun getPaging(subId : String, page: String) : Response<ResponComentarPaging>
    suspend fun postDiskusi(id : String,commentar : SaveCommentar): Response<ResponPostComentar>

    suspend fun likeDiskusi(docId: String?)  : Response<*>
    suspend fun deleteDiskusi(docId: String) : Response<*>
//    suspend fun updateDiskusi(value : SentEditDiskusis) : Response<*>

    fun cancelJobs()
}