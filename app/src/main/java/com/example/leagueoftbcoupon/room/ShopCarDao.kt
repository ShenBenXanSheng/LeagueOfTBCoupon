package com.example.leagueoftbcoupon.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

/**
 * 什本先生
 * 2022年5月27日
 * 购物车DAO层
 */
@Dao
interface ShopCarDao {
    @Insert
    fun insertShopCar(vararg shopCarData: ShopCarData)

    @Query("delete from ShopCarData")
    fun clearAllShopCar()

    @Query("delete from ShopCarData WHERE title = :title")
    fun deleteShopCar(title: String)

    @Query("select * from ShopCarData")
    fun queryAllShopCar(): LiveData<List<ShopCarData>>

    @Query("select * from ShopCarData WHERE title LIKE '%' || :keyWord || '%'")
    fun queryShopCar(keyWord: String): List<ShopCarData>
}