package com.app.jarimanis.data.datasource.models.diskusi


import com.google.gson.annotations.SerializedName

data class ResponDiskusi(
    @SerializedName("message")
    val message: String?, // comentar  delete!
    @SerializedName("status")
    val status: Int? // 200
)