package com.app.jarimanis.data.datasource.models.token


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class sent_token(
    @SerializedName("uid")
    val uid: String? // xvNScCATr4VtjtyoGwrMXdgVoRK2
) : Parcelable