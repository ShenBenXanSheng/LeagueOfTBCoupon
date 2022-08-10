package com.example.leagueoftbcoupon.domain

/**
 * 什本先生
 * 2022年5月24日
 * 统一适配器接受的数据
 * @see title 标题
 * @see clickUrl 图片
 * @see couponAmount 优惠券价格
 * @see zkFinalPrice 优惠前价格
 * @see volume 购买人数
 * @see clickUrl 没有优惠时的普通跳转链接
 * @see couponClickUrl 有优惠价格时的跳转链接
 */

interface BaseData {
    val title: String
    val zkFinalPrice: String
    val pictUrl: String
    val couponAmount: Int
    val volume: Int
    val clickUrl: String
    val couponClickUrl: String
}