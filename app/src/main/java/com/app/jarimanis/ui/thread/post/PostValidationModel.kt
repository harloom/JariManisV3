package com.app.jarimanis.ui.thread.post

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PostValidationModel(
    val title: String? = null,
    val content :  String? = null,
    val thumbnail: MutableList<String>? = null


) : Parcelable