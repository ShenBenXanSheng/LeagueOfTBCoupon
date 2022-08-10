package com.example.leagueoftbcoupon.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.widget.NestedScrollView
import com.example.leagueoftbcoupon.R
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * 什本先生
 * 2022年5月25日
 * 自定义NestedScrollView
 */
class HomeNestedScrollView : NestedScrollView {

    private var isSmartScroll = false
    private var isScrollSize: Int = 0
    private var headHeight: Int = 0
    lateinit var smartRefreshLayout: SmartRefreshLayout
    companion object {
        const val TAG = "HomeNestedScrollView"
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defaultAttr: Int) : super(
        context,
        attributeSet,
        defaultAttr
    ) {

    }
    //获取头部距离
    fun getHeadViewHeight(height: Int) {
        this.headHeight = height

    }


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        //如果滚动的距离小于头部距离，nv滚动,不嵌套滑动，加载关闭
        if (isScrollSize < headHeight) {
            scrollBy(dx, dy)
            consumed[0] = dx
            consumed[1] = dy
            isSmartScroll =false
            smartRefreshLayout.setEnableNestedScroll(isSmartScroll)
            smartRefreshLayout.setEnableLoadMore(false)

        } else {
            //如果滚动的距离大于头部距离，rv滚动,支持嵌套滑动，支持加载
            isSmartScroll = true
            smartRefreshLayout.setEnableNestedScroll(isSmartScroll)
            smartRefreshLayout.setEnableLoadMore(true)

        }
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }


    //获取滚动距离
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {

        this.isScrollSize = t

        super.onScrollChanged(l, t, oldl, oldt)
    }
    //获取刷新布局，将嵌套滚动，可以加载关闭
    fun getSmart(root: View) {
        smartRefreshLayout = root.findViewById(R.id.homeContentSmartRefresh)
        smartRefreshLayout.setEnableNestedScroll(false)
        smartRefreshLayout.setEnableLoadMore(false)
    }
}