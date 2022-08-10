package com.example.leagueoftbcoupon.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.leagueoftbcoupon.domain.dataBean.HandlerTicketData
import com.example.leagueoftbcoupon.domain.dataBean.TicketNeedData
import com.example.leagueoftbcoupon.repository.TicketRepository
import kotlinx.coroutines.launch
import java.lang.NullPointerException
/**
 * 什本先生
 * 2022年5月26日
 * 领券ViewModel
 */
class TicketViewModel : ViewModel() {
    private val ticketRepository by lazy {
        TicketRepository()
    }

    enum class RequestTickState {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    val ticketData = MutableLiveData<String>()
    val tickState = MutableLiveData<RequestTickState>()

    fun getTicketData(tickNeedData: HandlerTicketData.IsHandleTickNeedData) {
        val needData = TicketNeedData(tickNeedData.url, tickNeedData.title)
        tickState.postValue(RequestTickState.LOADING)

        viewModelScope.launch {
            try {
                val ticketList = ticketRepository.getTicketList(needData)
                if (ticketList.data.tbkTpwdCreateResponse.data.model.isNotBlank()) {

                    tickState.postValue(RequestTickState.SUCCESS)

                    ticketData.postValue(ticketList.data.tbkTpwdCreateResponse.data.model)
                } else {
                    tickState.postValue(RequestTickState.EMPTY)
                }
            } catch (e: Exception) {

                tickState.postValue(RequestTickState.ERROR)
                e.printStackTrace()
            }


        }
    }
}