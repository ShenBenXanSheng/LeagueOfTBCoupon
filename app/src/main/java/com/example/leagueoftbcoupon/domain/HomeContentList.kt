package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName

/**
 * 什本先生
 * 2022年5月24日
 * 首页内容的json数据
 */
class HomeContentList {
    data class HomeContent(
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: List<Data>,
        @SerializedName("message")
        val message: String,
        @SerializedName("success")
        val success: Boolean,
    )

    data class Data(
        @SerializedName("category_id")
        val categoryId: Int,
        @SerializedName("click_url")
        override val clickUrl: String,
        @SerializedName("commission_rate")
        val commissionRate: String,
        @SerializedName("coupon_amount")
        override val couponAmount: Int,
        @SerializedName("coupon_click_url")
        override val couponClickUrl: String,
        @SerializedName("coupon_end_time")
        val couponEndTime: String,
        @SerializedName("coupon_remain_count")
        val couponRemainCount: Int,
        @SerializedName("coupon_share_url")
        val couponShareUrl: String,
        @SerializedName("coupon_start_fee")
        val couponStartFee: String,
        @SerializedName("coupon_start_time")
        val couponStartTime: String,
        @SerializedName("coupon_total_count")
        val couponTotalCount: Int,
        @SerializedName("item_description")
        val itemDescription: String,
        @SerializedName("item_id")
        val itemId: String,
        @SerializedName("level_one_category_id")
        val levelOneCategoryId: Int,
        @SerializedName("level_one_category_name")
        val levelOneCategoryName: String,
        @SerializedName("pict_url")
        override val pictUrl: String,
        @SerializedName("reserve_price")
        val reservePrice: String,
        @SerializedName("seller_id")
        val sellerId: Long,
        @SerializedName("short_title")
        val shortTitle: Any,
        @SerializedName("small_images")
        val smallImages: SmallImages,
        @SerializedName("sub_title")
        val subTitle: String,
        @SerializedName("title")
        override val title: String,
        @SerializedName("tmall_play_activity_end_time")
        val tmallPlayActivityEndTime: Int,
        @SerializedName("tmall_play_activity_start_time")
        val tmallPlayActivityStartTime: Int,
        @SerializedName("user_type")
        val userType: Int,
        @SerializedName("volume")
        override val volume: Int,
        @SerializedName("zk_final_price")
        override val zkFinalPrice: String,
    ) : BaseData


    data class SmallImages(
        @SerializedName("string")
        val string: List<String>,
    )
}