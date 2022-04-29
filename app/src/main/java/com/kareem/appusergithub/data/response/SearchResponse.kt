package com.kareem.appusergithub.data.response

import com.google.gson.annotations.SerializedName
import com.kareem.appusergithub.data.model.UserItems


data class SearchResponse(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<UserItems> = emptyList(),
    val nextPage: Int? = null
)
