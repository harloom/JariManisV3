package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Thread(
    @SerializedName("message")
    val message: String?, // OK!
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: Int? // 200
) : Parcelable