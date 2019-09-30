package com.app.jarimanis.data.datasource.models.chats


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("emailUser")
    val emailUser: String?, // ilham solehudin1999@gmail.com
    @SerializedName("_id")
    val id: String?, // 5d862f33a50abb17501cd2df
    @SerializedName("nameUser")
    val nameUser: String?, // ilham solehudin 1999
    @SerializedName("numberPhone")
    val numberPhone: String?, // 082307304530
    @SerializedName("thumbail")
    val thumbail: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
)