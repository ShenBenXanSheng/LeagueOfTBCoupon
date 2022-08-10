package com.example.leagueoftbcoupon.repository

import com.example.leagueoftbcoupon.retrofit.RetrofitClient

class OnSellRepository {
    suspend fun getOnSell(url:String) = RetrofitClient.api.getOnSellData(url)
}