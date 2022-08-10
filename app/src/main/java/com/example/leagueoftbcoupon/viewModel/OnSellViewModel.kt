package com.example.leagueoftbcoupon.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.repository.OnSellRepository
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.domain.OnSellDataList
import com.example.leagueoftbcoupon.utils.UrlUtils
import kotlinx.coroutines.launch

class OnSellViewModel : ViewModel() {
    companion object {
        const val TAG = "OnSellViewModel"
    }

    private val onSellRepository by lazy {
        OnSellRepository()
    }
    private var currentPage = Constant.ON_SELL_CURRENT_PAGE

     val onSellDataList = MutableLiveData<List<OnSellDataList.MapData>>()

    enum class OnSellDataLoadStatus {
        LOADING, SUCCESS, EMPTY, ERROR, LOAD_MORE_ERROR, LOAD_MORE_SUCCESS, REFRESH_ERROR, REFRESH_SUCCESS
    }

     val onSellLoadStateList = MutableLiveData<OnSellDataLoadStatus>()

     val onSellLoadMoreList = MutableLiveData<List<OnSellDataList.MapData>>()
    fun getOnSellList() {
        val onSellDataUrl = UrlUtils.getOnSellDataUrl(currentPage)
        Log.d(TAG, "OnSell的url:${onSellDataUrl}")
        onSellLoadStateList.postValue(OnSellDataLoadStatus.LOADING)
        viewModelScope.launch {
            try {
                val onSellData = onSellRepository.getOnSell(onSellDataUrl)
                if (onSellData.data.tbkDgOptimusMaterialResponse.resultList.mapData.isNotEmpty()) {
                    onSellDataList.postValue(onSellData.data.tbkDgOptimusMaterialResponse.resultList.mapData)
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.SUCCESS)
                } else {
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onSellLoadStateList.postValue(OnSellDataLoadStatus.ERROR)
            }
        }
    }

    fun getOnSellLoadMoreList() {
        currentPage++
        if (currentPage == Constant.ON_SELL_MAX_PAGE) {
            currentPage = 1
        }
        val onSellDataUrl = UrlUtils.getOnSellDataUrl(currentPage)

        viewModelScope.launch {
            try {
                val onSellLoadMore = onSellRepository.getOnSell(onSellDataUrl)
                if (onSellLoadMore.data.tbkDgOptimusMaterialResponse.resultList.mapData.isNotEmpty()) {
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.LOAD_MORE_SUCCESS)
                    onSellLoadMoreList.postValue(onSellLoadMore.data.tbkDgOptimusMaterialResponse.resultList.mapData)
                } else {
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.LOAD_MORE_ERROR)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onSellLoadStateList.postValue(OnSellDataLoadStatus.LOAD_MORE_ERROR)
            }

        }
    }


    fun getOnSellRefreshList() {
        currentPage++
        if (currentPage == Constant.ON_SELL_MAX_PAGE) {
            currentPage = 1
        }
        val onSellDataUrl = UrlUtils.getOnSellDataUrl(currentPage)
        viewModelScope.launch {
            try {
                val onSellRefresh = onSellRepository.getOnSell(onSellDataUrl)
                if (onSellRefresh.data.tbkDgOptimusMaterialResponse.resultList.mapData.isNotEmpty()) {
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.REFRESH_SUCCESS)
                    onSellDataList.postValue(onSellRefresh.data.tbkDgOptimusMaterialResponse.resultList.mapData)
                } else {
                    onSellLoadStateList.postValue(OnSellDataLoadStatus.REFRESH_ERROR)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onSellLoadStateList.postValue(OnSellDataLoadStatus.REFRESH_ERROR)
            }
        }
    }
}