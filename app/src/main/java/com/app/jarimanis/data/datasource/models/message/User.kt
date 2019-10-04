package com.app.jarimanis.data.datasource.models.message


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("_id")
    val id: String? // 5d898f490d11c21dd89d3d5c
) : Parcelable