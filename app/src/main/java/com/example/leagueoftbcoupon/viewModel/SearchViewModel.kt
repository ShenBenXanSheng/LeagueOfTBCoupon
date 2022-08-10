package com.example.leagueoftbcoupon.viewModel

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.constant.Constant
import com.example.leagueoftbcoupon.domain.SearchDataList
import com.example.leagueoftbcoupon.domain.SearchRecommendDataList
import com.example.leagueoftbcoupon.repository.SearchRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.util.zip.GZIPOutputStream

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    val sp = application.getSharedPreferences("searchHistory", MODE_PRIVATE)
    private val searchRepository by lazy {
        SearchRepository()
    }
    val recommendDataList = MutableLiveData<List<SearchRecommendDataList.Data>>()

    enum class SearchDataStatus {
        LOADING, SUCCESS, EMPTY, ERROR, LOADED_MORE_SUCCESS, LOADED_MORE_ERROR
    }

    private var searchPage = Constant.SEARCH_PAGE

    private var currentKeyword = ""
    val searchDataStatusList = MutableLiveData<SearchDataStatus>()

    val searchDataList = MutableLiveData<List<SearchDataList.MapData>>()

    val searchLoadMoreDataList = MutableLiveData<List<SearchDataList.MapData>>()

    private val currentHistoryList = mutableListOf<String>()
    private var historySpList = mutableListOf<String>()
    val historyList = MutableLiveData<List<String>>()
    fun getRecommendKeyWord() {
        searchDataStatusList.postValue(SearchDataStatus.LOADING)
        viewModelScope.launch {
            try {
                val searchRecommendKeyWord = searchRepository.getSearchRecommendKeyWord()
                if (searchRecommendKeyWord.data.isNotEmpty()) {
                    searchDataStatusList.postValue(SearchDataStatus.SUCCESS)
                    recommendDataList.postValue(searchRecommendKeyWord.data)
                } else {
                    searchDataStatusList.postValue(SearchDataStatus.EMPTY)
                }
            } catch (e: Exception) {
                searchDataStatusList.postValue(SearchDataStatus.ERROR)
                e.printStackTrace()
            }
        }
    }

    fun getSearchDataList(keyword: String) {

        currentHistoryList.addAll(historySpList)
        if (!currentHistoryList.contains(keyword)) {

            currentHistoryList.add(keyword)
            if (currentHistoryList.size >= 10) {
                currentHistoryList.removeAt(currentHistoryList.size - 1)
            }

            sp.edit().putStringSet("historyKeywordList", currentHistoryList.toSet()).apply()
        }
        currentKeyword = keyword
        searchDataStatusList.postValue(SearchDataStatus.LOADING)
        viewModelScope.launch {
            try {
                val searchData = searchRepository.getSearchDataList(searchPage, keyword)
                if (searchData.data.tbkDgMaterialOptionalResponse.resultList.mapData.isNotEmpty()) {
                    searchDataStatusList.postValue(SearchDataStatus.SUCCESS)
                    searchDataList.postValue(searchData.data.tbkDgMaterialOptionalResponse.resultList.mapData)

                } else {
                    searchDataStatusList.postValue(SearchDataStatus.EMPTY)
                }
            } catch (e: Exception) {
                searchDataStatusList.postValue(SearchDataStatus.ERROR)
                e.printStackTrace()
            }
        }
    }

    fun getSearchLoadMoreDataList() {
        searchPage++
        viewModelScope.launch {
            try {
                val searchLoadMoreData =
                    searchRepository.getSearchDataList(searchPage, currentKeyword)
                if (searchLoadMoreData.data.tbkDgMaterialOptionalResponse.resultList.mapData.isNotEmpty()) {
                    searchLoadMoreDataList.postValue(searchLoadMoreData.data.tbkDgMaterialOptionalResponse.resultList.mapData)
                    searchDataStatusList.postValue(SearchDataStatus.LOADED_MORE_SUCCESS)
                } else {
                    searchDataStatusList.postValue(SearchDataStatus.LOADED_MORE_ERROR)

                }
            } catch (e: Exception) {
                e.printStackTrace()
                searchDataStatusList.postValue(SearchDataStatus.LOADED_MORE_ERROR)

            }

        }
    }

    fun clearHistory(){
        sp.edit().clear().apply()
        currentHistoryList.clear()
        historySpList.clear()

    }

    fun getSearchHistory() {
        val historySp = sp.getStringSet("historyKeywordList", null)
        if (historySp != null) {
            historySpList.clear()
            historySpList.addAll(historySp.toMutableList())
        }

        if (historySpList.size >= 10) {
            historySpList.removeAt(historySpList.size - 1)
        }
        historyList.postValue(historySpList)
    }
}