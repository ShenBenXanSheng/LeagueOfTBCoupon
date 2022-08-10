package com.example.leagueoftbcoupon.repository

import com.example.leagueoftbcoupon.domain.dataBean.TicketNeedData
import com.example.leagueoftbcoupon.retrofit.RetrofitClient

class TicketRepository {
    suspend fun getTicketList(ticketNeedData: TicketNeedData) =
        RetrofitClient.api.getTicket(ticketNeedData)


}