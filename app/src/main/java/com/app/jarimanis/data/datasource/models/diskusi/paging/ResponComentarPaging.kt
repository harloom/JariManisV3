package com.app.jarimanis.data.datasource.models.diskusi.paging


import com.google.gson.annotations.SerializedName

data class ResponComentarPaging(
    @SerializedName("message")
    val message: String?, // OK!
    @SerializedName("result")
    val result: Result?,
    @SerializedName("status")
    val status: Int? // 200
)