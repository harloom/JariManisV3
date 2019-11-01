package com.app.jarimanis.data.datasource.models.pemberitahuan


import com.google.gson.annotations.SerializedName

data class Doc(
    @SerializedName("createAt")
    val createAt: Long?, // 1572591955985
    @SerializedName("_id")
    val id: String?, // 5dbbda37cf886b00244c9d5c
    @SerializedName("_message")
    val message: String?, // harloom Akasaka Menuliskan diskusi di thread kamu
    @SerializedName("_read")
    val read: Boolean?, // false
    @SerializedName("_threadId")
    val threadId: ThreadId?,
    @SerializedName("updateAt")
    val updateAt: Long?, // 1572591955985
    @SerializedName("_userId")
    val userId: UserId?,
    @SerializedName("_fromId")
    val _fromId: UserId?,
    @SerializedName("__v")
    val v: Int? // 0
)