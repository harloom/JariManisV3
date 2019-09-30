package com.app.jarimanis.data.datasource.models.chats


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
    @SerializedName("_channelID")
    val channelID: ChannelID?,
    @SerializedName("_id")
    val id: String?, // 5d9192c75ae1aa1508f4858b
    @SerializedName("_userId")
    val userId: String?, // 5d838e3c4ccf792a449828fa
    @SerializedName("__v")
    val v: Int? // 0
) : Parcelable