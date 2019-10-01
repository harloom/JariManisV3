package com.app.jarimanis.data.datasource.models.token

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FirebaseToken(
    private val tokenFCM : String? =null
) : Parcelable
