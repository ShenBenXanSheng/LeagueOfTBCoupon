package com.example.leagueoftbcoupon.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.domain.Data

import com.example.leagueoftbcoupon.repository.HomeRepository
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * 什本先生
 * 2022年5月23日
 * 申请并处理首页标签数据
 */

class HomeCategoryViewModel : ViewModel() {
    private val homeCategoryRepository by lazy {
        HomeRepository()
    }

    enum class LoadCategoryStatus {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val categoryList = MutableLiveData<List<Data>>()
    val categoryLoadState = MutableLiveData<LoadCategoryStatus>()

    fun getCategoryList() {
        categoryLoadState.postValue(LoadCategoryStatus.LOADING)
        viewModelScope.launch {
            try {
                val categoryData = homeCategoryRepository.getCategoryData()
                if (categoryData.data.isNotEmpty()) {
                    categoryList.postValue(categoryData.data)
                    categoryLoadState.postValue(LoadCategoryStatus.SUCCESS)
                } else {
                    categoryLoadState.postValue(LoadCategoryStatus.EMPTY)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                categoryLoadState.postValue(LoadCategoryStatus.ERROR)
            }

        }
    }
}