package com.example.leagueoftbcoupon.retrofit

import com.example.leagueoftbcoupon.base.TaoBaoAPI
import com.example.leagueoftbcoupon.constant.Constant
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 什本先生
 * 2022年5月23日
 * RetrofitClient客户端
 */
object RetrofitClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(TaoBaoAPI::class.java)
}