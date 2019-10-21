package com.app.jarimanis.data.datasource.models.diskusi


import com.google.gson.annotations.SerializedName

data class ResponPostComentar(
    @SerializedName("message")
    val message: String?, // Comment created!
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: Int? // 201
)