package com.app.jarimanis.data.datasource.models.thread


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doc(
    @SerializedName("category")
    val category: String?, // 5d838945114e12216ce87e12
    @SerializedName("content")
    val content: String?, // Pembahasan Topik 3
    @SerializedName("title")
    val title : String?,
    @SerializedName("createAt")
    val createAt: Long?, // 2019-09-23T08:21:17.330Z
    @SerializedName("_id")
    val id: String?, // 5d8880877118de149c0756c7
    @SerializedName("images")
    val images: MutableList<Image?>?,
    @SerializedName("likes")
    val likes: MutableList<Like?>?,
    @SerializedName("updateAt")
    val updateAt: Long?, // 2019-09-23T08:21:17.330Z
    @SerializedName("user")
    val user: UserT?, // 5d838e3c4ccf792a449828fa
    @SerializedName("__v")
    val v: Int?, // 3
    @SerializedName("videos")
    val videos: MutableList<Video?>?,
    @SerializedName("views")
    val views: MutableList<Like?>?,
    @SerializedName("isLikes")
    var isLikes : Boolean? =null,
    @SerializedName("diskusiCount")
    var diskusiCount : Int = 0
):Parcelable