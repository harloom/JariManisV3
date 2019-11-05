
package com.app.jarimanis.data.datasource.api

import com.app.jarimanis.data.datasource.models.ChannelRespon
import com.app.jarimanis.data.datasource.models.SentEditThreads
import com.app.jarimanis.data.datasource.models.chats.Chats
import com.app.jarimanis.data.datasource.models.diskusi.ResponDiskusi
import com.app.jarimanis.data.datasource.models.diskusi.ResponPostComentar
import com.app.jarimanis.data.datasource.models.diskusi.SaveCommentar
import com.app.jarimanis.data.datasource.models.diskusi.paging.ResponComentarPaging
import com.app.jarimanis.data.datasource.models.kategori.Category
import com.app.jarimanis.data.datasource.models.message.ReciveMessage
import com.app.jarimanis.data.datasource.models.message.Sender
import com.app.jarimanis.data.datasource.models.message.SentNewChannel
import com.app.jarimanis.data.datasource.models.pemberitahuan.Pemberitahuan
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


    @DELETE("thread/{id}")
    suspend fun  deleteThreads(
        @Path("id") idDoc :  String?
    ): Response<*>

    @PUT("thread")
    suspend fun updateThread(
        @Body editThread  : SentEditThreads
    ) : Response<*>


    @PUT("thread/like/{id}")
    suspend fun likeThread(
        @Path("id") idDoc: String?
    ): Response<*>

    /* commentary*/


    @GET("diskusi")
    suspend fun getDiskusi(
        @Query("tid") id : String ,@Query("page") pageId : String?
    ) : Response<ResponComentarPaging>

    @POST("diskusi/{id}")
    suspend fun postComentar(
        @Path("id") id:String?,
        @Body saveCommentar: SaveCommentar
    ) :Response<ResponPostComentar>


    @DELETE("diskusi/{id}")
    suspend fun deleteComentar(
        @Path("id") id : String
    ):Response<ResponDiskusi>



    /* pemberitahuan */
    @GET("pemberitahuan")
    suspend fun  getPemberitahuan (
      @Query("page") pageId : String?
    ) : Response<Pemberitahuan>


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
