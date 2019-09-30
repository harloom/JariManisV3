package com.app.jarimanis.data.datasource.models.chats


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChannelID(
    @SerializedName("_id")
    val id: String?, // 5d9192c65ae1aa1508f4858a
    @SerializedName("userList")
    val userList: List<User?>?,
    @SerializedName("__v")
    val v: Int? // 0
) : Parcelable