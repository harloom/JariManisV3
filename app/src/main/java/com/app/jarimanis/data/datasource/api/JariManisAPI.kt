package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.thread.Threads
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JariManisAPI {
    @GET("category")
        suspend  fun listCategory() : Response<Category>


    @GET("thread")
    suspend fun getThreads(
        @Query("category") subId:String?,
        @Query("page") pageId : String?
    ) : Response<Threads>
}
