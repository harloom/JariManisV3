package com.app.jarimanis.data.datasource.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class UserRegister(
    @Transient
    val _id: String? = null,
    val _uid: String? = null,
    val nameUser: String? = null,
    val birthDayUser: String? = null,
    val emailUser: String? = null,
    val thumbail: String? = null,
    val numberPhone: String? = null
) : Parcelable
