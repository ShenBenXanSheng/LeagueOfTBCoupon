package com.example.leagueoftbcoupon.repository

import com.example.leagueoftbcoupon.retrofit.RetrofitClient

/**
 * 什本先生
 * 2022年5月27日
 * 精选界面仓库层
 */
class SelectedRepository {
    suspend fun getRepositoryId() = RetrofitClient.api.getSelectedCategories()
    suspend fun getSelectedData(url: String) = RetrofitClient.api.getSelectedData(url)
}