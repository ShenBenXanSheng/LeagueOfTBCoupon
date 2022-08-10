package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName
/**
 * 什本先生
 * 2022年5月27日
 * 精选界面标题json数据
 */
class SelectedCategories {
    data class Categories(
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
        @SerializedName("favorites_id")
        val favoritesId: Int,
        @SerializedName("favorites_title")
        val favoritesTitle: String,
        @SerializedName("type")
        val type: Int
    )
}