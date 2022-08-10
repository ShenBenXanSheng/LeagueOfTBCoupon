package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName

class SearchRecommendDataList {
    data class SearchKeyWordList(
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("message")
        val message: String,
        @SerializedName("success")
        val success: Boolean
    )

    data class Data(
        @SerializedName("createTime")
        val createTime: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("keyword")
        val keyword: String
    )

}