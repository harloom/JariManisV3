package com.app.jarimanis.data.datasource.models.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    @SerializedName("message")
    val message: String?, // Ok
    @SerializedName("result")
    val result: Result?
) : Parcelable