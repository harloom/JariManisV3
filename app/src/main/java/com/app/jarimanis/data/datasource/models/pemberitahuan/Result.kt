package com.app.jarimanis.data.datasource.models.pemberitahuan


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("docs")
    val docs: List<Doc?>?,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean?, // false
    @SerializedName("hasPrevPage")
    val hasPrevPage: Boolean?, // false
    @SerializedName("limit")
    val limit: Int?, // 10
    @SerializedName("nextPage")
    val nextPage: String?, // null
    @SerializedName("page")
    val page: Int?, // 1
    @SerializedName("pagingCounter")
    val pagingCounter: Int?, // 1
    @SerializedName("prevPage")
    val prevPage: String?, // null
    @SerializedName("totalDocs")
    val totalDocs: Int?, // 1
    @SerializedName("totalPages")
    val totalPages: Int? // 1
)