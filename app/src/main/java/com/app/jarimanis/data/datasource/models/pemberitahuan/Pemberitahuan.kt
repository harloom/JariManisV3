package com.app.jarimanis.data.datasource.models.pemberitahuan


import com.google.gson.annotations.SerializedName

data class Pemberitahuan(
    @SerializedName("message")
    val message: String?, // Ok
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: Int? // 200
)