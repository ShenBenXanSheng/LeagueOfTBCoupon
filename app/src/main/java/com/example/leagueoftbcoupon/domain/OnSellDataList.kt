package com.example.leagueoftbcoupon.domain


import com.google.gson.annotations.SerializedName

class OnSellDataList {

    data class OnSellData(
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
        val resultList: ResultList
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
        val shortTitle: String,
        @SerializedName("small_images")
        val smallImages: SmallImages,
        @SerializedName("sub_title")
        val subTitle: String,
        @SerializedName("title")
        override val title: String,
        @SerializedName("tmall_play_activity_end_time")
        val tmallPlayActivityEndTime: Long,
        @SerializedName("tmall_play_activity_start_time")
        val tmallPlayActivityStartTime: Long,
        @SerializedName("user_type")
        val userType: Int,
        @SerializedName("volume")
        override val volume: Int,
        @SerializedName("zk_final_price")
        override val zkFinalPrice: String
    ):BaseData

    data class SmallImages(
        @SerializedName("string")
        val string: List<String>
    )
}