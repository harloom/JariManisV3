package com.app.jarimanis.data.datasource.models.thread


import com.google.gson.annotations.SerializedName

data class UploadThread(
    @SerializedName("category")
    val category: String?, // 5d838945114e12216ce87e12
    @SerializedName("content")
    val content: String?, // Pembahasan Topik 3
    @SerializedName("images")
    val images: List<ImageX?>?,
    @SerializedName("title")
    val title: String?, // Title 3
    @SerializedName("videos")
    val videos: List<VideoX?>?
)