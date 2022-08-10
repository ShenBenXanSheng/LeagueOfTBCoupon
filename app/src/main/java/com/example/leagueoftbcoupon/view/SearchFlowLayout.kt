package com.example.leagueoftbcoupon.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.domain.SearchRecommendDataList
import com.example.leagueoftbcoupon.utils.SizeUtil

class SearchFlowLayout : ViewGroup {

    private lateinit var onFlowItemClickListener: OnFlowItemClickListener
    private val distance = SizeUtil.dip2px(10f)
    private val dataList = mutableListOf<String>()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defaultAttrs: Int) : super(
        context,
        attributeSet,
        defaultAttrs
    ) {

    }

    private lateinit var lines: MutableList<View>
    private val linesList = mutableListOf<List<View>>()
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount != 0) {
            linesList.clear()
            lines = mutableListOf()
            linesList.add(lines)
            for (i in 0 until childCount) {
                val childAt = getChildAt(i)
                if (childAt.visibility != View.GONE) {
                    measureChild(childAt, widthMeasureSpec, heightMeasureSpec)

                    if (!canBeAdd(
                            lines,
                            childAt.measuredWidth,
                            MeasureSpec.getSize(widthMeasureSpec)
                        )
                    ) {
                        lines = mutableListOf()
                        linesList.add(lines)
                    }
                    lines.add(childAt)
                }
            }
            val child0 = getChildAt(0)
            setMeasuredDimension(
                widthMeasureSpec,
                linesList.size * child0.measuredHeight + linesList.size * distance + distance
            )
        }
    }

    private fun canBeAdd(
        lines: List<View>,
        measuredWidth: Int,
        widthMeasureSpec: Int
    ): Boolean {
        var total = 0
        lines.forEach {
            total += it.measuredWidth + distance
        }
        total += measuredWidth + distance

        return widthMeasureSpec >= total
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount != 0) {
            val child0 = getChildAt(0)
            var currentLeft = distance
            var currentTop = distance
            var currentRight = 0
            var currentBottom = child0.measuredHeight + distance
            linesList.forEach { lines ->
                lines.forEach {
                    currentRight += it.measuredWidth + distance
                    it.layout(currentLeft, currentTop, currentRight, currentBottom)
                    currentLeft = currentRight + distance
                }
                currentLeft = distance
                currentRight = 0
                currentTop += child0.measuredHeight + distance
                currentBottom += child0.measuredHeight + distance
            }
        }

    }

    fun setData(it: List<String>) {
        dataList.clear()
        dataList.addAll(it)
        removeAllViews()
        dataList.forEach { keyword ->
            val inflate = LayoutInflater.from(this.context).inflate(
                R.layout.item_search_flow_text, this, false
            ) as TextView
            inflate.text = keyword
            inflate.setOnClickListener {
                if (this::onFlowItemClickListener.isInitialized) {
                    onFlowItemClickListener.flowItemClick(inflate.text.toString())
                }
            }
            addView(inflate)
        }

    }

    fun setOnFlowItemClickListener(onFlowItemClickListener: OnFlowItemClickListener) {
        this.onFlowItemClickListener = onFlowItemClickListener
    }

    interface OnFlowItemClickListener {
        fun flowItemClick(keyword: String)
    }

}