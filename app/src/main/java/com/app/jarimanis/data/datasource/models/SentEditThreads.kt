package com.app.jarimanis.data.datasource.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SentEditThreads(
    @SerializedName("id")
    val id : String? = null,
    val title : String? =null,
    val content : String? = null

) : Parcelable
