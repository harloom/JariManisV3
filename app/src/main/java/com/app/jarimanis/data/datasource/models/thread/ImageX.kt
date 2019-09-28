package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageX(
    @SerializedName("_url")
    val _url: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
) : Parcelable