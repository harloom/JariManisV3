package com.app.jarimanis.data.datasource.models


import com.google.gson.annotations.SerializedName

data class ResponServer(
    @SerializedName("message")
    val message: String?, // category created!
    @SerializedName("status")
    val status: Int? // 201
)