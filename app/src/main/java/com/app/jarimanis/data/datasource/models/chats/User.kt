package com.app.jarimanis.data.datasource.models.chats


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("createAt")
    val createAt: String?, // 2019-09-30T05:27:54.292Z
    @SerializedName("_id")
    val user: UserChat?,
    @SerializedName("updateAt")
    val updateAt: String? // 2019-09-30T05:27:54.292Z
) : Parcelable