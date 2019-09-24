package com.app.jarimanis.data.datasource.models.profile


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Result(
  @SerializedName("birthDayUser")
  val birthDayUser: String?, // 2019-01-19T00:00:00.000Z
  @SerializedName("emailUser")
  val emailUser: String?, // ilham solehudin1999@gmail.com
  @SerializedName("_id")
  val id: String?, // 5d862f33a50abb17501cd2df
  @SerializedName("loginAt")
  val loginAt: String?, // 2019-09-21T14:09:51.916Z
  @SerializedName("nameUser")
  val nameUser: String?, // ilham solehudin 1999
  @SerializedName("numberPhone")
  val numberPhone: String?, // 082307304530
  @SerializedName("thumbail")
  val thumbail: String? // https://s3.zerochan.net/Fate.Grand.Order.240.2104974.jpg
) : Parcelable