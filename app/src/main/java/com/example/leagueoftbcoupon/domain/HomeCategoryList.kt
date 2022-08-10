package com.example.leagueoftbcoupon.domain
import com.google.gson.annotations.SerializedName

/**
 * 什本先生
 * 2022年5月23日
 * 首页标签的json数据
 */
data class HomeCategoryList(
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
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
)