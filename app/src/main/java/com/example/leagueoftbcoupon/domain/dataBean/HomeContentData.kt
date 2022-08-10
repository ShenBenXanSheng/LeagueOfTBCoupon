package com.example.leagueoftbcoupon.domain.dataBean

/**
 * 什本先生
 * 2022年5月24日
 * 首页内容rv item需要的数据
 * @param title 标题
 * @param cover 图片
 * @param coupon 优惠券价格
 * @param beforePrice 优惠前价格
 * @param afterPrice 优惠后价格
 * @param shopCount 购买人数
 */
data class HomeContentData(
    val title: String,
    val cover: Any,
    val coupon: String,
    val beforePrice: String,
    val afterPrice: String,
    val shopCount: String
)
