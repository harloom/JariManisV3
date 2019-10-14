package com.app.jarimanis.data.datasource.models.profile

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sent_change (
    val nameUser  :String? = null,
    val date : String? = null,
    val numberPhone : String? =null,
    val thumbail : String? = null
) : Parcelable


