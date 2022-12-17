package com.example.leagueoftbcoupon.base


import com.example.leagueoftbcoupon.domain.*
import com.example.leagueoftbcoupon.domain.dataBean.TicketNeedData
import retrofit2.http.*

/**
 * 什本先生
 * 2022年5月23日
 * retrofit的api层
 */
interface TaoBaoAPI {
    @GET("discovery/categories")
    suspend fun getHomeCategories(): HomeCategoryList

    @GET("discovery/{materialId}/{page}")
    suspend fun getHomeContent(
        @Path("materialId") materialId: Int,
        @Path("page") page: Int
    ): HomeContentList.HomeContent

    @POST("tpwd")
    suspend fun getTicket(@Body ticketNeedData: TicketNeedData): TicketList.TicketData


    //废弃
    @GET("recommend/categories")
    suspend fun getSelectedCategories(): SelectedCategories.Categories

    //废弃
    @GET
    suspend fun getSelectedData(@Url selectedDataUrl: String): SelectedDataList.SelectedData

    @GET
    suspend fun getOnSellData(@Url onSellDataUrl: String): OnSellDataList.OnSellData

    @GET("search/recommend")
    suspend fun getSearchRecommendKeyWord(): SearchRecommendDataList.SearchKeyWordList

    @GET("search")
    suspend fun getSearchData(
        @Query("page") page: Int,
        @Query("keyword") keyword: String
    ): SearchDataList.SearchData
}