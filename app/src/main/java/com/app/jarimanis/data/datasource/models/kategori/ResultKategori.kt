package com.app.jarimanis.data.datasource.models.kategori


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResultKategori(
  @SerializedName("category")
  val category: String?, // Umum
  @SerializedName("createAt")
  val createAt: String?, // 2019-09-19T13:57:11.385Z
  @SerializedName("icon")
  val icon: String?, // https://firebasestorage.googleapis.com/v0/b/jarimanis-project.appspot.com/o/icon%2Ficons8_family_48px.png?alt=media&token=ea279024-f217-4985-9070-7773404e1418
  @SerializedName("_id")
  val id: String?, // 5d838945114e12216ce87e12
  @SerializedName("updateAt")
  val updateAt: String?, // 2019-09-19T13:57:11.385Z
  @SerializedName("__v")
  val v: Int? // 0
) : Parcelable