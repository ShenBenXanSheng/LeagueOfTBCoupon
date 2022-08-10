package com.example.leagueoftbcoupon.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.repository.ShopCarRepository
import com.example.leagueoftbcoupon.room.ShopCarDao
import com.example.leagueoftbcoupon.room.ShopCarData
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * 什本先生
 * 2022年5月27日
 * 购物车ViewModel
 */

class ShopCarViewModel(application: Application) : AndroidViewModel(application) {
    private val shopCarRepository by lazy {
        ShopCarRepository(application.applicationContext)
    }
    val queryShopList = MutableLiveData<List<ShopCarData>>()
    fun getShopCarDao(): ShopCarDao {
        return shopCarRepository.getShopDao()
    }

    @DelicateCoroutinesApi
    fun insertShopCarData(baseData: BaseData) {
        shopCarRepository.insertShopData(baseData)
    }

    @DelicateCoroutinesApi
    fun clearShopCarData() {
        shopCarRepository.clearShopData()
    }

    @DelicateCoroutinesApi
    fun deleteShopCarData(baseData: BaseData) {
        shopCarRepository.deleteShopData(baseData)
    }

    fun queryAllShopCarData() = shopCarRepository.queryAllShopData()

    @OptIn(DelicateCoroutinesApi::class)
    fun queryShopCarDataByKeyword(keyword: String) {
        GlobalScope.launch{
            val queryShopData = shopCarRepository.queryShopData(keyword)
            queryShopList.postValue(queryShopData)
        }

    }
}