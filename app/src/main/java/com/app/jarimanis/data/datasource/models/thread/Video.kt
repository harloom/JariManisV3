package com.app.jarimanis.data.datasource.models.thread


import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("createAt")
    val createAt: String?, // 2019-09-21T14:12:40.312Z
    @SerializedName("_id")
    val id: String?, // 5d862fe2eb62612c70ff7d17
    @SerializedName("updateAt")
    val updateAt: String?, // 2019-09-21T14:12:40.312Z
    @SerializedName("_url")
    val url: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
)