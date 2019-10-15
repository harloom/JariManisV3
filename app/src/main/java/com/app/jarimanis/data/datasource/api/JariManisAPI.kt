
package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.ChannelRespon
import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.app.jarimanis.data.datasource.models.message.SentNewChannel
import com.app.jarimanis.data.datasource.models.thread.Threads
import com.app.jarimanis.data.datasource.models.thread.UploadThread
import retrofit2.Response
import retrofit2.http.*


interface JariManisAPI {
    @GET("category")
        suspend  fun listCategory() : Response<Category>



    /*  thread api */

    @GET("thread")
    suspend fun getThreads(
        @Query("category") subId:String?,
        @Query("page") pageId : String?
    ) : Response<Threads>

    @GET("thread/u/{uid}")
    suspend fun getUserThreads(
        @Path("uid") uid:String?,
        @Query("page") pageId : String?
    ) : Response<Threads>
    @POST("thread")
    suspend fun postThreads(
        @Body thread: UploadThread
    ): Response<UploadThread>







    /*  chat api */
    @POST("channel/ck")
    suspend fun  cekChannelExits(@Body  sender : SentNewChannel) : Response<ChannelRespon>

    @GET("channel-user")
    suspend fun getChannelUser(): Response<Chats>

    @POST("room")
    suspend fun postMessage(@Body  message : Sender) : Response<ReciveMessage>

    @POST("room/new")
    suspend fun postNewChannelAndMessage(@Body  sender : SentNewChannel) : Response<ChannelRespon>


}
