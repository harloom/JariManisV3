
package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface JariManisAPI {
    @GET("category")
        suspend  fun listCategory() : Response<Category>


    @GET("thread")
    suspend fun getThreads(
        @Query("category") subId:String?,
        @Query("page") pageId : String?
    ) : Response<Threads>

    @POST("thread")
    suspend fun postThreads(
        @Body thread: UploadThread
    ): Response<UploadThread>




    /*  chat api */

    @GET("channel-user")
    suspend fun getChannelUser(): Response<Chats>
}
