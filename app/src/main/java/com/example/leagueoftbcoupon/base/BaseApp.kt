package com.example.leagueoftbcoupon.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Handler

class BaseApp : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
        var handle:Handler?=null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = baseContext
        handle = Handler()
    }
}