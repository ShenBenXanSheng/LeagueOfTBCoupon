package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName

/**
 * 什本先生
 * 2022年5月28日
 * 精选界面内容json数据
 */
class SelectedDataList {
    data class SelectedData(
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("message")
        val message: String,
        @SerializedName("success")
        val success: Boolean
    )

    data class Data(
        @SerializedName("tbk_dg_optimus_material_response")
        val tbkDgOptimusMaterialResponse: TbkDgOptimusMaterialResponse
    )

    data class TbkDgOptimusMaterialResponse(
        @SerializedName("is_default")
        val isDefault: String,
        @SerializedName("request_id")
        val requestId: String,
        @SerializedName("result_list")
        val resultList: ResultList,
        @SerializedName("total_count")
        val totalCount: Int
    )

    data class ResultList(
        @SerializedName("map_data")
        val mapData: List<MapData>
    )

    data class MapData(
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
        @SerializedName("coupon_info")
        val couponInfo: String,
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
        @SerializedName("item_id")
        val itemId: Long,
        @SerializedName("level_one_category_id")
        val levelOneCategoryId: Int,
        @SerializedName("nick")
        val nick: String,
        @SerializedName("pict_url")
        override val pictUrl: String,
        @SerializedName("reserve_price")
        val reservePrice: String,
        @SerializedName("seller_id")
        val sellerId: Long,
        @SerializedName("shop_title")
        val shopTitle: String,
        @SerializedName("small_images")
        val smallImages: SmallImages,
        @SerializedName("title")
        override val title: String,
        @SerializedName("user_type")
        val userType: Int,
        @SerializedName("volume")
        override val volume: Int,
        @SerializedName("white_image")
        val whiteImage: String,
        @SerializedName("zk_final_price")
        override val zkFinalPrice: String
    ):BaseData

    data class SmallImages(
        @SerializedName("string")
        val string: List<String>
    )
}