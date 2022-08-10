package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName

class SearchDataList {

    data class SearchData(
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
        @SerializedName("tbk_dg_material_optional_response")
        val tbkDgMaterialOptionalResponse: TbkDgMaterialOptionalResponse
    )

    data class TbkDgMaterialOptionalResponse(
        @SerializedName("request_id")
        val requestId: String,
        @SerializedName("result_list")
        val resultList: ResultList,
        @SerializedName("total_results")
        val totalResults: Int
    )

    data class ResultList(
        @SerializedName("map_data")
        val mapData: List<MapData>
    )

    data class MapData(
        @SerializedName("category_id")
        val categoryId: Int,
        @SerializedName("category_name")
        val categoryName: String,
        @SerializedName("commission_rate")
        val commissionRate: String,
        @SerializedName("commission_type")
        val commissionType: String,
        @SerializedName("coupon_amount")
        override val couponAmount: Int,
        @SerializedName("coupon_end_time")
        val couponEndTime: String,
        @SerializedName("coupon_id")
        val couponId: String,
        @SerializedName("coupon_info")
        val couponInfo: String,
        @SerializedName("coupon_remain_count")
        val couponRemainCount: Int,
        @SerializedName("coupon_share_url")
        override val couponClickUrl: String,
        @SerializedName("coupon_start_fee")
        val couponStartFee: String,
        @SerializedName("coupon_start_time")
        val couponStartTime: String,
        @SerializedName("coupon_total_count")
        val couponTotalCount: Int,
        @SerializedName("include_dxjh")
        val includeDxjh: String,
        @SerializedName("include_mkt")
        val includeMkt: String,
        @SerializedName("info_dxjh")
        val infoDxjh: String,
        @SerializedName("item_description")
        val itemDescription: String,
        @SerializedName("item_id")
        val itemId: Long,
        @SerializedName("item_url")
        val itemUrl: String,
        @SerializedName("jdd_num")
        val jddNum: Int,
        @SerializedName("jdd_price")
        val jddPrice: Any,
        @SerializedName("level_one_category_id")
        val levelOneCategoryId: Int,
        @SerializedName("level_one_category_name")
        val levelOneCategoryName: String,
        @SerializedName("nick")
        val nick: String,
        @SerializedName("num_iid")
        val numIid: Long,
        @SerializedName("oetime")
        val oetime: Any,
        @SerializedName("ostime")
        val ostime: Any,
        @SerializedName("pict_url")
        override val pictUrl: String,
        @SerializedName("presale_deposit")
        val presaleDeposit: String,
        @SerializedName("presale_end_time")
        val presaleEndTime: Int,
        @SerializedName("presale_start_time")
        val presaleStartTime: Int,
        @SerializedName("presale_tail_end_time")
        val presaleTailEndTime: Int,
        @SerializedName("presale_tail_start_time")
        val presaleTailStartTime: Int,
        @SerializedName("provcity")
        val provcity: String,
        @SerializedName("real_post_fee")
        val realPostFee: String,
        @SerializedName("reserve_price")
        val reservePrice: String,
        @SerializedName("seller_id")
        val sellerId: Long,
        @SerializedName("shop_dsr")
        val shopDsr: Int,
        @SerializedName("shop_title")
        val shopTitle: String,
        @SerializedName("short_title")
        val shortTitle: String,
        @SerializedName("small_images")
        val smallImages: SmallImages,
        @SerializedName("title")
        override val title: String,
        @SerializedName("tk_total_commi")
        val tkTotalCommi: String,
        @SerializedName("tk_total_sales")
        val tkTotalSales: String,
        @SerializedName("url")
        override val clickUrl: String,
        @SerializedName("user_type")
        val userType: Int,
        @SerializedName("volume")
        override val volume: Int,
        @SerializedName("white_image")
        val whiteImage: String,
        @SerializedName("x_id")
        val xId: String,
        @SerializedName("zk_final_price")
        override val zkFinalPrice: String,


        ) : BaseData

    data class SmallImages(
        @SerializedName("string")
        val string: List<String>
    )
}