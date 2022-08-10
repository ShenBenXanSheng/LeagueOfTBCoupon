package com.example.leagueoftbcoupon.repository

import com.example.leagueoftbcoupon.retrofit.RetrofitClient

class SearchRepository {
    suspend fun getSearchRecommendKeyWord() = RetrofitClient.api.getSearchRecommendKeyWord()

    suspend fun getSearchDataList(page: Int, keyWord: String) =
        RetrofitClient.api.getSearchData(page, keyWord)
}