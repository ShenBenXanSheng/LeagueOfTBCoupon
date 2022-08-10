package com.example.leagueoftbcoupon.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 什本先生
 * 2022年5月26日
 * 控制viewModel的baseActivity
 */
abstract class BaseViewModelActivity<T : ViewModel> : BaseActivity() {
    lateinit var currentViewModel: T

    override fun initViewModel() {
        super.initViewModel()
        currentViewModel = ViewModelProvider(this)[setViewModelClass()]
    }

    abstract fun setViewModelClass(): Class<T>


}