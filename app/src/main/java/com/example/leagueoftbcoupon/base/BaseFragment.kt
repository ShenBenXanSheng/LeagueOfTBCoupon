package com.example.leagueoftbcoupon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.leagueoftbcoupon.R

/**
 * 什本先生
 * 2022年5月23日
 * baseFragment 主要控制状态的显示
 */
abstract class BaseFragment : Fragment() {
    private lateinit var contentContainer: FrameLayout
    private lateinit var loginView: View
    private lateinit var successView: View
    private lateinit var emptyView: View
    private lateinit var errorView: View

    enum class LoadViewStatus {
        LOADING, SUCCESS, EMPTY, ERROR
    }
    //根据外部传来的参数，判断加载哪个view
    open fun switchDisplayView(loadViewStatus: LoadViewStatus) {
        hideAllView()
        when (loadViewStatus) {
            LoadViewStatus.LOADING -> {
                loginView.visibility = View.VISIBLE
            }
            LoadViewStatus.SUCCESS -> {
                successView.visibility = View.VISIBLE
            }
            LoadViewStatus.EMPTY -> {
                emptyView.visibility = View.VISIBLE
            }
            LoadViewStatus.ERROR -> {
                errorView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = setRootView(inflater, container)
        contentContainer = rootView.findViewById(R.id.contentContainerFl)
        loadAllView()
        initViewModel()
        initView()
        initDataListener()
        initListener()
        return rootView
    }

    //将所有布局加载到frameLayout里
    private fun loadAllView() {
        loginView = loadView(R.layout.state_loading)
        successView = setSuccessView(contentContainer)
        emptyView = loadView(R.layout.state_empty)
        errorView = loadView(R.layout.state_error)
        errorView.setOnClickListener {
            reLoadData()
        }
        contentContainer.apply {
            addView(loginView)
            addView(successView)
            addView(emptyView)
            addView(errorView)
        }
        hideAllView()
    }
    //加载成功后隐藏所有
    private fun hideAllView() {
        loginView.visibility = View.GONE
        successView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
    }



    //把各个状态加载进来
    private fun loadView(layoutId: Int): View {
        return LayoutInflater.from(context).inflate(layoutId, contentContainer, false)
    }

    //根布局，如有需要可以修改，但修改的布局里必须又一个frameLayout，id是contentContainerFl
    open fun setRootView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    //成功加载的布局
    abstract fun setSuccessView(contentContainer: FrameLayout): View

    abstract fun reLoadData()//重新加载数据

    open fun initListener() {

    }

    open fun initViewModel() {

    }

    open fun initView() {

    }

    open fun initDataListener() {

    }
}