package com.example.leagueoftbcoupon.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
/**
 * 什本先生
 * 2022年5月27日
 * 购物车数据库层
 */
@Database(version = 1, entities = [ShopCarData::class], exportSchema = false)
abstract class ShopCarDataBase : RoomDatabase() {
    abstract fun getShopDao(): ShopCarDao

    companion object {
        private var instance: ShopCarDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): ShopCarDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context, ShopCarDataBase::class.java, "shopCar.db").build()
                .apply {
                    instance = this
                }
        }
    }
}