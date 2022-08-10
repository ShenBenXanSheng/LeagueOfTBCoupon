package com.example.leagueoftbcoupon.repository

import android.content.Context
import android.util.Log
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.room.ShopCarDao
import com.example.leagueoftbcoupon.room.ShopCarData
import com.example.leagueoftbcoupon.room.ShopCarDataBase
import kotlinx.coroutines.*

/**
 * 什本先生
 * 2022年5月27日
 * 购物车仓库层
 */
class ShopCarRepository(val context: Context) {
    private val shopDao = ShopCarDataBase.getDataBase(context).getShopDao()

    private fun handlerShopCarData(baseData: BaseData): ShopCarData {
        return ShopCarData(
            baseData.title,
            baseData.zkFinalPrice,
            baseData.pictUrl,
            baseData.couponAmount,
            baseData.volume,
            baseData.clickUrl,
            baseData.couponClickUrl
        )
    }

    fun getShopDao(): ShopCarDao {
        return shopDao
    }

    @DelicateCoroutinesApi
    fun insertShopData(baseData: BaseData) {
        val handlerShopCarData = handlerShopCarData(baseData)
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                shopDao.insertShopCar(handlerShopCarData)
            }
        }
    }

    @DelicateCoroutinesApi
    fun clearShopData() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                shopDao.clearAllShopCar()
            }
        }
    }

    @DelicateCoroutinesApi
    fun deleteShopData(baseData: BaseData) {

        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                shopDao.deleteShopCar(baseData.title)
            }
        }
    }


    fun queryAllShopData() = shopDao.queryAllShopCar()

    fun queryShopData(keyWord: String): List<ShopCarData> {

        return shopDao.queryShopCar(keyWord)
    }
}