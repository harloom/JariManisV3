package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Like(
    @SerializedName("createAt")
    val createAt: String?, // 2019-09-23T08:49:37.369Z
    @SerializedName("_id")
    val id: UserT?, // 5d838e3c4ccf792a449828fa
    @SerializedName("updateAt")
    val updateAt: String? // 2019-09-23T08:49:37.369Z
) : Parcelable