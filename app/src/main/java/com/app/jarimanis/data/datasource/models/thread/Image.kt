package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    @SerializedName("createAt")
    val createAt: String?, // 2019-09-23T08:21:17.330Z
    @SerializedName("_id")
    val id: String?, // 5d8880877118de149c0756c9
    @SerializedName("updateAt")
    val updateAt: String?, // 2019-09-23T08:21:17.330Z
    @SerializedName("_url")
    val url: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
) : Parcelable