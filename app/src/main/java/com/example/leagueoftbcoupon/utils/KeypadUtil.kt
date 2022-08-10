package com.example.leagueoftbcoupon.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.leagueoftbcoupon.base.BaseApp

object KeypadUtil {
    fun hideKeypad(view: View){
        val inputMethodService =
            BaseApp.mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.hideSoftInputFromWindow(view.windowToken,0)
    }

    fun showKeypad(view: View){
        val inputMethodService =
            BaseApp.mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodService.showSoftInput(view,0)
    }
}