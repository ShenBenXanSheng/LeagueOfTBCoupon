package com.example.leagueoftbcoupon.adapter

import android.annotation.SuppressLint
import com.example.leagueoftbcoupon.domain.BaseData
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.utils.UrlUtils

/**
 * 什本先生
 * 2022年5月24日
 * 轮播图适配器
 */
class ContentPageAdapter : PagerAdapter() {
    companion object {
        const val TAG = "ContentPageAdapter"
    }

    private lateinit var onContentPageClickListener: OnContentPageClickListener
     val dataList = mutableListOf<BaseData>()

    @SuppressLint("ClickableViewAccessibility")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        //进行取模运算，让容器只能拿到0 1 2 3 ，实现伪循环

        val data = dataList[position % dataList.size]
        //拿到轮播图的item布局
        val layout = LayoutInflater.from(container.context)
            .inflate(R.layout.item_home_content_page, container, false)
        //处理图片，避免加载空图片
        val cover = if (data.pictUrl.isNotBlank()) {
            UrlUtils.getCoverUrl(data.pictUrl)
        } else {
            R.mipmap.yuanshen
        }
        //找到控件
        val imageView = layout.findViewById<ImageView>(R.id.pageImage)
        //点击事件
        imageView.setOnClickListener {
            onContentPageClickListener.pageClick(data)
        }

        imageView.setOnLongClickListener {
            onContentPageClickListener.pageLongClick(data)
            true
        }
        //设置图片
        Glide.with(container.context).load(cover).into(imageView)
        //将布局添加到container中
        container.addView(layout)
        //返回该布局
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)

    }

    override fun getCount(): Int {
        //count设置为很大的数
        return Integer.MAX_VALUE
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view == `object`
    }

    fun setData(it: List<BaseData>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
            notifyDataSetChanged()
        }

    }

    //外部调用这个，响应点击
    fun setOnContentPageClickListener(onContentPageClickListener: OnContentPageClickListener) {
        this.onContentPageClickListener = onContentPageClickListener
    }

    interface OnContentPageClickListener {
        fun pageClick(baseData: BaseData)
        fun pageLongClick(baseData: BaseData)
    }
}