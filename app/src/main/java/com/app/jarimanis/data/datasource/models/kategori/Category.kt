package com.app.jarimanis.data.datasource.models.kategori



import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("message")
    val message: String?, // OK!
    @SerializedName("result")
    val resultKategori: List<ResultKategori?>?,
    @SerializedName("status")
    val status: Int? // 200
) : Parcelable