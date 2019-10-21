package com.app.jarimanis.data.datasource.models.diskusi.paging


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("docs")
    val docs: List<Doc?>?,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean?, // true
    @SerializedName("hasPrevPage")
    val hasPrevPage: Boolean?, // false
    @SerializedName("limit")
    val limit: Int?, // 3
    @SerializedName("nextPage")
    val nextPage: String?, // 2
    @SerializedName("page")
    val page: Int?, // 1
    @SerializedName("pagingCounter")
    val pagingCounter: Int?, // 1
    @SerializedName("prevPage")
    val prevPage: String?, // null
    @SerializedName("totalDocs")
    val totalDocs: Int?, // 4
    @SerializedName("totalPages")
    val totalPages: Int? // 2
)