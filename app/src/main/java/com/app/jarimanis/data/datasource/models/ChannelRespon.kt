package com.app.jarimanis.data.datasource.models


import com.google.gson.annotations.SerializedName

data class ChannelRespon(
    @SerializedName("channelId")
    val channelId: String?, // null
    @SerializedName("message")
    val message: String?, // OK data is null
    @SerializedName("status")
    val status: Int? // 410
)