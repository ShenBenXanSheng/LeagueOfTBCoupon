package com.example.leagueoftbcoupon.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * 什本先生
 * 2022年5月24日
 * 自定义轮播图
 */
class HomeContentViewPager : ViewPager {

    private var lastX = 0f
    private var lastY = 0f

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {

    }

    companion object {
        const val TAG = "HomeContentViewPager"
    }

    //处理viewpager和viewpager2 viewpager和smartRefresh的事件冲突
    /**
     * @fun requestDisallowInterceptTouchEvent 返回true，阻止父容器拦截事件 返回false，不阻止
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            //先获取点击的x轴和y轴
            val x = ev.x
            val y = ev.y
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    //down手势，不阻止父容器的事件
                    parent.requestDisallowInterceptTouchEvent(false)
                }
                MotionEvent.ACTION_MOVE -> {
                    //move手势，如果y轴在移动，事件不给viewpager，如果是x在动，事件才给viewpager
                    if (abs(x - lastX) < abs(y - lastY)) {
                        parent.requestDisallowInterceptTouchEvent(false)
                    } else {
                        parent.requestDisallowInterceptTouchEvent(true)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    parent.requestDisallowInterceptTouchEvent(false)
                }

            }
            //手放开之前最后一次手势的x y轴
            lastX = x
            lastY = y
        }
        return super.dispatchTouchEvent(ev)
    }



    //处理轮播图的自动轮播
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                //如果手在屏幕上，停止自动
                stopTemp()
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //如果手离开，开始自动
                startTemp()
            }
        }
        return super.onTouchEvent(ev)
    }

    private var isTemp = false//是否开启自动
    private val itemTemp = object : Runnable {
        override fun run() {
            //如果自动为true，当前页面4秒加一次，设置回去
            if (isTemp) {
                currentItem++
            }

            currentItem = currentItem
            postDelayed(this, 4000)
        }

    }

    private fun startTemp() {
        isTemp = true
        postDelayed(itemTemp, 4000)

    }

    private fun stopTemp() {
        isTemp = false
        removeCallbacks(itemTemp)
    }

    //界面可见，开始自动轮播
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startTemp()
    }

    //界面不可见，取消自动
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTemp()
    }
}