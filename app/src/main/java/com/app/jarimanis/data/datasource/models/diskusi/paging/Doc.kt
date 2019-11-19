package com.app.jarimanis.data.datasource.models.diskusi.paging


import com.app.jarimanis.data.datasource.models.thread.UserT
import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("content")
    val content: String?, // test notif
    @SerializedName("_image")
    val image: String?, // test notif
    @SerializedName("_video")
    val video: String?, // test notif
    @SerializedName("createAt")
    val createAt: Long?, // 1571382998282
    @SerializedName("_id")
    val id: String?, // 5da966e714578512586748c8
    @SerializedName("_threadId")
    val threadId: String?, // 5da84b0be0ec4b0024789228
    @SerializedName("updateAt")
    val updateAt: Long?, // 1571382998282
    @SerializedName("_userId")
    val user: UserT?, // 5d838e3c4ccf792a449828fa
    @SerializedName("isLikes")
    var isLikes : Boolean? =false,
    @SerializedName("__v")
    val v: Int? // 0
)