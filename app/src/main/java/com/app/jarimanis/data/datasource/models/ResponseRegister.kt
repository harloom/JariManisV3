package com.app.jarimanis.data.datasource.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseRegister (
  @Expose
  @SerializedName("messege")
  val messege : String? = null,
  @Expose
  @SerializedName("results")
  val results  : UserRegister? = null

) : Parcelable