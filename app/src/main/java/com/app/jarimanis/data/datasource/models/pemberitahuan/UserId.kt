package com.app.jarimanis.data.datasource.models.pemberitahuan


import com.google.gson.annotations.SerializedName

data class UserId(
    @SerializedName("_id")
    val id: String?, // 5d862f33a50abb17501cd2df
    @SerializedName("nameUser")
    val nameUser: String?, // ilham solehudin
    @SerializedName("thumbail")
    val thumbail: String? // https://firebasestorage.googleapis.com/v0/b/jarimanis-project.appspot.com/o/users%2F5d862f33a50abb17501cd2df?alt=media&token=a8696d4b-3cbc-4d9e-accc-5c85369b932a
)