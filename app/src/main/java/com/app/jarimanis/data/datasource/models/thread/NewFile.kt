package com.app.jarimanis.data.datasource.models.thread

import android.os.Parcelable
import com.google.firebase.storage.StorageReference
import kotlinx.android.parcel.Parcelize


object Status{
    const val InProgress = 1
    const val ErrorUpload = 0
    const val Success = 2

}

@Parcelize
data class NewFile (
    val idThread :  String,
    val jenis : Int,
    val path : String
): Parcelable

@Parcelize
data class UrlUpload(
    val _url : String
): Parcelable
@Parcelize
data class UploadStatus(
    val status : Int,
    val progress : Int?=null,
    val message : String,
    val storage: String?=null
) : Parcelable
