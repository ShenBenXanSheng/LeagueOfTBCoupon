package com.example.leagueoftbcoupon.view

import android.annotation.SuppressLint
import com.example.leagueoftbcoupon.domain.BaseData
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.adapter.ContentPageAdapter
import com.example.leagueoftbcoupon.constant.Constant
import kotlinx.android.synthetic.main.layout_indication.view.*
import java.lang.Exception

/**
 * 什本先生
 * 2022年5月24日
 * 指示点和轮播图的组合控件
 */
class IndicationLayout : RelativeLayout {
    private lateinit var onPageClickListener: OnPageClickListener

    //轮播图的适配器
    private val contentPageAdapter by lazy {
        ContentPageAdapter()
    }

    //四张图片代表四个指示点，"imageViewMedium"代表取消选中指示点
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView
    private lateinit var imageView4: ImageView
    private lateinit var imageViewMedium: ImageView

    private val dataList = mutableListOf<BaseData>()

    companion object {
        const val TAG = "IndicationLayout"
    }

    private var layout: View

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defaultAttr: Int) : super(
        context,
        attributeSet,
        defaultAttr
    ) {
        layout = LayoutInflater.from(context).inflate(R.layout.layout_indication, this, false)
        addView(layout)
        initView(layout)

    }

    private fun initView(layout: View?) {

        layout.apply {
            imageView1 = findViewById(R.id.im1)
            imageView2 = findViewById(R.id.im2)
            imageView3 = findViewById(R.id.im3)
            imageView4 = findViewById(R.id.im4)
            //默认第一个指示点选择
            imageView1.isSelected = true
            //给控制指示点的图片赋值
            imageViewMedium = imageView1

            homeViewPage.apply {
                //设置轮播图适配器
                adapter = contentPageAdapter

                //伪循环轮播图，起始位置为500，就可以反方向滑动
                currentItem = Constant.HOME_VIEWPAGER_CURRENT_ITEM

                //预加载3
                offscreenPageLimit = Constant.OFFSCREEN_PAGE_LIMIT
                //设置轮播图的监听
                addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {

                    }

                    override fun onPageSelected(position: Int) {
                        //将imageViewMedium改为false,如果离开当前页面，取消选择
                        imageViewMedium.isSelected = false
                        if (dataList.size != 0) {
                            //当前的position是500往后，让他取模容器大小，就可以 0 1 2 3 循环更新

                            when (position % Constant.HOME_VIEWPAGER_LIST_SIZE) {
                                0 -> {
                                    imageView1.isSelected = true
                                    imageViewMedium = imageView1
                                }

                                1 -> {
                                    imageView2.isSelected = true
                                    imageViewMedium = imageView2
                                }

                                2 -> {
                                    imageView3.isSelected = true
                                    imageViewMedium = imageView3
                                }

                                3 -> {
                                    imageView4.isSelected = true
                                    imageViewMedium = imageView4
                                }
                            }
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {}
                })
            }
        }

        contentPageAdapter.setOnContentPageClickListener(
            object :
                ContentPageAdapter.OnContentPageClickListener {
                override fun pageClick(baseData: BaseData) {
                    onPageClickListener.onPageClick(baseData)
                }

                override fun pageLongClick(baseData: BaseData) {
                    onPageClickListener.onPageLongClick(baseData)
                }
            })
    }

    //将数据传给轮播图的适配器
    fun setData(it: List<BaseData>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
            contentPageAdapter.setData(it)
        }

    }

    //外部通过该方法响应点击事件
    fun setOnPageClickListener(onPageClickListener: OnPageClickListener) {
        this.onPageClickListener = onPageClickListener
    }

    interface OnPageClickListener {
        fun onPageClick(baseData: BaseData)
        fun onPageLongClick(baseData: BaseData)
    }
}