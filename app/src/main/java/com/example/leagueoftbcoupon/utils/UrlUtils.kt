package com.example.leagueoftbcoupon.utils

object UrlUtils {
    fun getCoverUrl(coverUrl: String): String {
        return if (coverUrl.startsWith("https:") || coverUrl.startsWith("http:")) {
            coverUrl
        } else {
            "https:${coverUrl}"
        }
    }

    fun getTicketUrl(ticketUrl: String): String {
        return if (ticketUrl.startsWith("https:") || ticketUrl.startsWith("http:")) {
            ticketUrl
        } else {
            "https:${ticketUrl}"
        }
    }

    fun getSelectedDataUrl(categoryId: Int): String {
        return "https://api.sunofbeaches.com/shop/recommend/${categoryId}"
    }

    fun getOnSellDataUrl(page: Int): String {
        return "https://api.sunofbeaches.com/shop/onSell/${page}"
    }
}