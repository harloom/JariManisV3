package com.app.jarimanis.data.datasource.models.chats


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Chats(
    @SerializedName("message")
    val message: String?, // OK!
    @SerializedName("result")
    val result: List<Result?>?,
    @SerializedName("status")
    val status: Int? // 200
) : Parcelable