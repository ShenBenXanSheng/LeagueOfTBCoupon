package com.example.leagueoftbcoupon.adapter

import com.example.leagueoftbcoupon.domain.BaseData
import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.databinding.ItemHomeContentBinding
import com.example.leagueoftbcoupon.domain.dataBean.HomeContentData
import com.example.leagueoftbcoupon.room.ShopCarData
import com.example.leagueoftbcoupon.utils.UrlUtils

/**
 * 什本先生
 * 2022年5月24日
 * 首页内容rv的适配器
 */
class CurrencyDataAdapter : RecyclerView.Adapter<CurrencyDataAdapter.InnerHolder>() {
    private lateinit var onCurrencyItemClickListener: OnCurrencyItemClickListener
    private val dataList = mutableListOf<BaseData>()
    private val searchList = mutableListOf<BaseData>()
    class InnerHolder(itemView: View, val dataBinding: ItemHomeContentBinding) :
        RecyclerView.ViewHolder(itemView) {

    }

    companion object {
        //通过自定义命名的方式，设置图片和文字中划线
        @SuppressLint("CheckResult")
        @JvmStatic
        @BindingAdapter("cover")
        fun setHomeContentCover(itemView: ImageView, cover: Any) {
            val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
            Glide.with(itemView.context).load(cover).apply(requestOptions).into(itemView)
        }

        @JvmStatic
        @BindingAdapter("strikethrough")

        fun setHomeBeforePrice(itemView: TextView, price: String) {
            itemView.text = price
            itemView.paint.flags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        }

        const val TAG = "HomeContentRvAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val dataBinding = DataBindingUtil.inflate<ItemHomeContentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_home_content,
            parent,
            false
        )
        //经过把rv的高度设置为warp，动态布局高度后，rv实现复用
        Log.d(TAG, "onCreateViewHolder")
        return InnerHolder(dataBinding.root, dataBinding)
    }

    //绑定数据
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val contentData = dataList[position]
        handleData(holder, contentData)


    }

    private fun handleData(holder: InnerHolder, contentData: BaseData) {
        contentData.apply {
            if (title.isNullOrEmpty() && pictUrl.isNullOrEmpty()) {
                holder.dataBinding.homeItemContainer.visibility = View.GONE
            }

            val shopTitle = title
            val cover = if (pictUrl.isNotBlank()) {
                UrlUtils.getCoverUrl(pictUrl)
            } else {
                R.mipmap.paimeng
            }

            var coupon = ""
            val beforePrice = "¥${String.format("%.2f", zkFinalPrice.toDouble())}"
            var afterPrice = ""
            if (couponAmount != null) {
                coupon = "省${couponAmount}元"
                afterPrice = "券后价:${String.format("%.2f", zkFinalPrice.toDouble() - couponAmount)}"
            } else {
                coupon = "暂无优惠券"
                holder.dataBinding.homeItemBeforePrice.visibility = View.GONE
            }

            val shopCount = "${volume}人已购买"
            holder.dataBinding.contentData =
                HomeContentData(shopTitle, cover, coupon, beforePrice, afterPrice, shopCount)

            //点击事件
            holder.dataBinding.homeItemContainer.apply {
                setOnClickListener {
                    onCurrencyItemClickListener.onItemClickListener(contentData)
                }

                setOnLongClickListener {
                    onCurrencyItemClickListener.onItemLongClickListener(contentData)
                    true
                }
            }
        }
    }

    override fun getItemCount() = dataList.size

    //正常加载和刷新的数据
    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<BaseData>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

    //加载更多的数据
    @SuppressLint("NotifyDataSetChanged")
    fun setLoadMoreData(it: List<BaseData>?) {
        if (it != null) {
            dataList.addAll(it)
            //只通知原来item最后面和当前数据最后面那段距离的数据更新，避免卡顿
            notifyItemChanged(dataList.size, it.size)
        }
    }

    fun setCurrencyItemClickListener(onCurrencyItemClickListener: OnCurrencyItemClickListener) {
        this.onCurrencyItemClickListener = onCurrencyItemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSearchData(it: List<ShopCarData>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

    interface OnCurrencyItemClickListener {
        fun onItemClickListener(baseData: BaseData)

        fun onItemLongClickListener(baseData: BaseData)
    }
}