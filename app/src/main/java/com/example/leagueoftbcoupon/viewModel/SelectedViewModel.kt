package com.example.leagueoftbcoupon.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.domain.SelectedCategories
import com.example.leagueoftbcoupon.domain.SelectedDataList
import com.example.leagueoftbcoupon.repository.SelectedRepository
import com.example.leagueoftbcoupon.utils.UrlUtils
import kotlinx.coroutines.launch

/**
 * 什本先生
 * 2022年5月27日
 * 精选界面ViewModel
 */
class SelectedViewModel : ViewModel() {
    private val selectedRepository by lazy {
        SelectedRepository()
    }

    enum class SelectedDataStatus {
        LOADING, SUCCESS, ERROR, EMPTY,
        CONTENT_LOADING, CONTENT_SUCCESS, CONTENT_ERROR, CONTENT_EMPTY
    }

    val repositoryIdList = MutableLiveData<List<SelectedCategories.Data>>()
    val selectedStatus = MutableLiveData<SelectedDataStatus>()
    val selectedDataList = MutableLiveData<List<SelectedDataList.MapData>>()

    fun getSelectedCategory() {
        selectedStatus.postValue(SelectedDataStatus.LOADING)
        viewModelScope.launch {
            try {
                val repositoryId = selectedRepository.getRepositoryId()
                if (repositoryId.data.isNotEmpty()) {
                    repositoryIdList.postValue(repositoryId.data)
                    selectedStatus.postValue(SelectedDataStatus.SUCCESS)
                } else {
                    selectedStatus.postValue(SelectedDataStatus.EMPTY)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                selectedStatus.postValue(SelectedDataStatus.ERROR)
            }
        }
    }

    fun getSelectedContentByCategoryId(categoryId: Int) {
        selectedStatus.postValue(SelectedDataStatus.CONTENT_LOADING)
        val selectedDataUrl = UrlUtils.getSelectedDataUrl(categoryId)
        viewModelScope.launch {
            try {
                val selectedData = selectedRepository.getSelectedData(selectedDataUrl)
                if (selectedData.data != null
                    && selectedData.data.tbkDgOptimusMaterialResponse.resultList.mapData.isNotEmpty()
                ) {
                    selectedDataList.postValue(selectedData.data.tbkDgOptimusMaterialResponse.resultList.mapData)
                    selectedStatus.postValue(SelectedDataStatus.CONTENT_SUCCESS)

                } else {
                    selectedStatus.postValue(SelectedDataStatus.CONTENT_EMPTY)
                }

            } catch (e: Exception) {
                e.printStackTrace()
                selectedStatus.postValue(SelectedDataStatus.CONTENT_ERROR)
            }
        }
    }
}