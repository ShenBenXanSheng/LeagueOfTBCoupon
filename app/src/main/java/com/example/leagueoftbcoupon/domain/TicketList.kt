package com.example.leagueoftbcoupon.domain

import com.google.gson.annotations.SerializedName

class TicketList {

    data class TicketData(
        @SerializedName("code")
        val code: Int,
        @SerializedName("data")
        val `data`: Data,
        @SerializedName("message")
        val message: String,
        @SerializedName("success")
        val success: Boolean
    )

    data class Data(
        @SerializedName("tbk_tpwd_create_response")
        val tbkTpwdCreateResponse: TbkTpwdCreateResponse
    )

    data class TbkTpwdCreateResponse(
        @SerializedName("data")
        val `data`: DataX,
        @SerializedName("request_id")
        val requestId: String
    )

    data class DataX(
        @SerializedName("model")
        val model: String
    )
}