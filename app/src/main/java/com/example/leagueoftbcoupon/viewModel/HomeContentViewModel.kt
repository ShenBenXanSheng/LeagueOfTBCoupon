package com.example.leagueoftbcoupon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.constant.Constant

import com.example.leagueoftbcoupon.domain.HomeContentList
import com.example.leagueoftbcoupon.repository.HomeRepository
import kotlinx.coroutines.launch
import java.lang.Exception

import kotlin.random.Random

/**
 * 什本先生
 * 2022年5月24日
 * @param  currentPage 请求页面的页码
 * @param  categoryId 请求页面的id
 * 根据viewpager2适配器传过来的categoryId和自己设置的页码，请求数据
 */
class HomeContentViewModel : ViewModel() {
    private val homeRepository by lazy {
        HomeRepository()
    }
    private var currentPage = Constant.HOME_CONTENT_PAGE

    enum class HomeContentLoadStatus {
        LOADING, SUCCESS, EMPTY, ERROR
    }

    val contentLoadStatus = MutableLiveData<HomeContentLoadStatus>()

    val contentDataList = MutableLiveData<List<HomeContentList.Data>>()

    val contentLoadMoreList = MutableLiveData<List<HomeContentList.Data>>()

    val contentViewPageList = MutableLiveData<List<HomeContentList.Data>>()

    fun getHomeContentData(categoryId: Int) {
        contentLoadStatus.postValue(HomeContentLoadStatus.LOADING)
        viewModelScope.launch {
            try {
                val contentData = homeRepository.getContentData(categoryId, currentPage)
                if (contentData.data.isNotEmpty()) {
                    contentDataList.postValue(contentData.data)
                    contentLoadStatus.postValue(HomeContentLoadStatus.SUCCESS)
                } else {
                    contentLoadStatus.postValue(HomeContentLoadStatus.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                contentLoadStatus.postValue(HomeContentLoadStatus.ERROR)
            }
        }
    }

    //加载更多
    fun getHomeLoadMoreData(categoryId: Int) {
        //调用一次，页码++，如果页码加到最大了，重制为1
        currentPage++
        if (currentPage == Constant.HOME_MAX_PAGE) {
            currentPage = Constant.HOME_CONTENT_PAGE
        }
        viewModelScope.launch {
            try {
                val contentLoadMoreData = homeRepository.getContentData(categoryId, currentPage)
                if (contentLoadMoreData != null) {
                    contentLoadMoreList.postValue(contentLoadMoreData.data)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //刷新
    fun getHomeRefreshData(categoryId: Int) {
        currentPage++
        if (currentPage == Constant.HOME_MAX_PAGE) {
            currentPage = Constant.HOME_CONTENT_PAGE
        }
        viewModelScope.launch {
            try {
                val contentRefreshData = homeRepository.getContentData(categoryId, currentPage)
                if (contentRefreshData != null) {
                    contentDataList.postValue(contentRefreshData.data)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }



    //轮播图数据
    fun getHomeViewPagerData(categoryId: Int) {

        contentLoadStatus.postValue(HomeContentLoadStatus.LOADING)
        viewModelScope.launch {
            try {
                val contentViewPageData = homeRepository.getContentData(categoryId, Constant.HOME_CONTENT_PAGE)
                if (contentViewPageData != null) {
                    contentViewPageList.postValue(contentViewPageData.data)
                    contentLoadStatus.postValue(HomeContentLoadStatus.SUCCESS)
                } else {
                    contentLoadStatus.postValue(HomeContentLoadStatus.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                contentLoadStatus.postValue(HomeContentLoadStatus.ERROR)
            }
        }
    }
}