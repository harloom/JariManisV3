package com.app.jarimanis.data.datasource.models.message


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sender(
    @SerializedName("ci")
    val ci: String?, // 5d9568f09daa2100c47ff9b5
    @SerializedName("_id")
    val _id : String?,
    @SerializedName("message")
    val message: String?, // Hello Word!.. 6
    @SerializedName("image")
    val image  : String? =null,
    @SerializedName("video")
    val video : String?=null,

    val status : Int?
) : Parcelable