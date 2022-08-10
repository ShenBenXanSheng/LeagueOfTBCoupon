package com.example.leagueoftbcoupon.repository

import com.example.leagueoftbcoupon.retrofit.RetrofitClient

/**
 * 什本先生
 * 2022年5月23日
 *  主页请求数据的仓库层
 */
class HomeRepository {
    suspend fun getCategoryData() = RetrofitClient.api.getHomeCategories()
    suspend fun getContentData(categoryId: Int, page: Int) =
        RetrofitClient.api.getHomeContent(categoryId, page)
}