package com.example.leagueoftbcoupon.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 什本先生
 * 2022年5月23日
 * 初始化viewModel的baseFragment
 */
abstract class BaseViewModelFragment<vm:ViewModel>:BaseFragment() {

    lateinit var currentViewModel: vm

    override fun initViewModel() {
        super.initViewModel()
        currentViewModel = ViewModelProvider(this)[setViewModelClass()]
    }

    abstract fun setViewModelClass():Class<vm>
}