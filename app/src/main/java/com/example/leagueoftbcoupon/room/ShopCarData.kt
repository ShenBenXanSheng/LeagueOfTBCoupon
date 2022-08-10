package com.example.leagueoftbcoupon.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.leagueoftbcoupon.domain.BaseData

/**
 * 什本先生
 * 2022年5月27日
 * 购物车数据层
 */
@Entity
data class ShopCarData(
    @PrimaryKey(autoGenerate = true) val id: Int,
    override val title: String,
    override val zkFinalPrice: String,
    override val pictUrl: String,
    override val couponAmount: Int,
    override val volume: Int,
    override val clickUrl: String,
    override val couponClickUrl: String
) :BaseData{
    constructor(
        title: String,
        zkFinalPrice: String,
        pictUrl: String,
        couponAmount: Int,
        volume: Int,
        clickUrl: String,
        couponClickUrl: String
    ) : this(0, title, zkFinalPrice, pictUrl, couponAmount, volume, clickUrl, couponClickUrl)
}
