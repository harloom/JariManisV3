package com.app.jarimanis.data.datasource.models.diskusi


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("content")
    val content: String?, // test save pemberitahuan
    @SerializedName("createAt")
    val createAt: Long?, // 1571637994724
    @SerializedName("_id")
    val id: String?, // 5dad4b595702d609a49f205a
    @SerializedName("_threadId")
    val threadId: String?, // 5da84b0be0ec4b0024789228
    @SerializedName("updateAt")
    val updateAt: Long?, // 1571637994724
    @SerializedName("_userId")
    val userId: String?, // 5d862f33a50abb17501cd2df
    @SerializedName("__v")
    val v: Int? // 0
)