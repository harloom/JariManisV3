package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserT(
    @SerializedName("_id")
    val id: String?, // 5d838e3c4ccf792a449828fa
    @SerializedName("nameUser")
    val nameUser: String?, // ilham solehudin
    @SerializedName("thumbail")
    val thumbail: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
) : Parcelable