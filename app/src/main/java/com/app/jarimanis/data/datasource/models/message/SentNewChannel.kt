package com.app.jarimanis.data.datasource.models.message


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SentNewChannel(
    @SerializedName("message")
    val message: String?, // OKe Test
    @SerializedName("userList")
    val userList: List<User?>?,
    @SerializedName("image")
    val image  : String?,
    @SerializedName("video")
    val video : String?,
    val status : Int?
) : Parcelable