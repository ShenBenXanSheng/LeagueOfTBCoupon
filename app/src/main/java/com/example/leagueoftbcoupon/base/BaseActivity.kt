package com.example.leagueoftbcoupon.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.leagueoftbcoupon.R

/**
 * 什本先生
 * 2022年5月26日
 * baseActivity，很具请求数据的状态控制显示哪个layouut
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var activityContentContainer: FrameLayout
    private lateinit var loadingView: View
    private lateinit var successView: View
    private lateinit var emptyView: View
    private lateinit var errorView: View

    enum class ActivityLoadState {
        LOADING, SUCCESS, ERROR, EMPTY
    }

    fun updateState(activityLoadState: ActivityLoadState) {
        hideAllView()
        when (activityLoadState) {
            ActivityLoadState.LOADING -> {
                loadingView.visibility = View.VISIBLE
            }

            ActivityLoadState.SUCCESS -> {
                successView.visibility = View.VISIBLE
            }

            ActivityLoadState.EMPTY -> {
                emptyView.visibility = View.VISIBLE
            }

            ActivityLoadState.ERROR -> {
                errorView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = setRootView()
        setContentView(rootView)
        activityContentContainer = rootView.findViewById(R.id.activityContentContainerFl)
        loadAllView()
        hideAllView()
        initViewModel()
        initView()

        initDataListener()
        initListener()
    }

    private fun hideAllView() {
        loadingView.visibility = View.GONE
        successView.visibility = View.GONE
        emptyView.visibility = View.GONE
        errorView.visibility = View.GONE
    }

    private fun loadAllView() {
        loadingView = loadView(R.layout.state_loading)
        successView = setSuccessView(activityContentContainer)
        emptyView = loadView(R.layout.state_empty)
        errorView = loadView(R.layout.state_error)
        errorView.setOnClickListener {
            reloadData()
        }
        activityContentContainer.apply {
            addView(loadingView)
            addView(successView)
            addView(emptyView)
            addView(errorView)
        }


    }

    open fun setRootView(): View {
        return LayoutInflater.from(this).inflate(R.layout.activity_base, null)
    }

    private fun loadView(layoutId: Int): View {
        return LayoutInflater.from(this).inflate(layoutId, activityContentContainer, false)
    }

    abstract fun setSuccessView(container:FrameLayout): View

    abstract fun reloadData()

    open fun initListener() {

    }

    open fun initDataListener() {

    }
    open fun initViewModel(){

    }
    open fun initView() {

    }
}