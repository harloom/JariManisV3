package com.app.jarimanis.data.datasource.models.message


import com.google.gson.annotations.SerializedName

data class Sender(
    @SerializedName("ci")
    val ci: String?, // 5d9568f09daa2100c47ff9b5
    @SerializedName("message")
    val message: String?, // Hello Word!.. 6
    @SerializedName("image")
    val image  : String?,
    @SerializedName("video")
    val video : String?
)