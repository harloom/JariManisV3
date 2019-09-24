package com.app.jarimanis.data.datasource.models.thread


import com.google.gson.annotations.SerializedName

data class Threads(
    @SerializedName("message")
    val message: String?, // OK!
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: Int? // 200
)