package com.example.leagueoftbcoupon.domain.dataBean

import android.os.Parcelable
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.utils.UrlUtils
import kotlinx.android.parcel.Parcelize
import java.lang.NullPointerException

/**
 * 什本先生
 * 2022年5月26日
 * 处理url和图片，用来请求口令
 */
object HandlerTicketData {
    fun handleTickNeedData(baseData: BaseData): IsHandleTickNeedData {
        if (baseData != null) {
            val coverUrl = UrlUtils.getCoverUrl(baseData.pictUrl)

            val ticketUrl =
                if (baseData.couponClickUrl != null&&baseData.couponClickUrl.isNotBlank() ) {
                    UrlUtils.getTicketUrl(baseData.couponClickUrl)
                } else {
                    println(baseData.clickUrl)
                    UrlUtils.getTicketUrl(baseData.clickUrl)
                }
            return IsHandleTickNeedData(ticketUrl, coverUrl, baseData.title)
        }
        throw NullPointerException("没有接收到点击传来的data数据")

    }

    @Parcelize
    data class IsHandleTickNeedData(val url: String, val picUrl: String, val title: String) :
        Parcelable


}
