package com.example.leagueoftbcoupon.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
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
import com.example.leagueoftbcoupon.databinding.ItemOnSellDataBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.OnSellDataList
import com.example.leagueoftbcoupon.domain.dataBean.OnSellDataBean
import com.example.leagueoftbcoupon.utils.UrlUtils

class OnSellAdapter : RecyclerView.Adapter<OnSellAdapter.InnerHolder>() {
    companion object {
        @JvmStatic
        @BindingAdapter("on_sell_cover")
        fun setCover(itemView: ImageView, cover: Any) {
            val requestOptions = RequestOptions.bitmapTransform(RoundedCorners(20))
            Glide.with(itemView).load(cover).apply(requestOptions).into(itemView)
        }

        @JvmStatic
        @BindingAdapter("on_sell_before_price")
        fun setBeforePrice(itemView: TextView, price: String) {
            itemView.text = price
            itemView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    private lateinit var onItemClickListener: OnItemClickListener
    private val dataList = mutableListOf<BaseData>()

    class InnerHolder(itemView: View, val dataBinding: ItemOnSellDataBinding) :
        RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val dataBinding = DataBindingUtil.inflate<ItemOnSellDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_on_sell_data,
            parent,
            false,
        )

        return InnerHolder(dataBinding.root, dataBinding)
    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val data = dataList[position]

        data.apply {

            if (pictUrl == null && title == null) {
                holder.dataBinding.itemOnSellDataContainer.visibility = View.GONE
            } else {
                holder.dataBinding.itemOnSellDataContainer.visibility = View.VISIBLE
                val cover = if (pictUrl.isNotEmpty()) {
                    UrlUtils.getCoverUrl(pictUrl)
                } else {
                    R.mipmap.paimeng
                }
                val beforePrice = String.format("%.2f", zkFinalPrice.toDouble())
                val afterPrice = zkFinalPrice.toDouble() - couponAmount.toDouble()

                holder.dataBinding.onSellData =
                    OnSellDataBean(
                        cover,
                        title,
                        "¥${beforePrice}",
                        "券后价:${String.format("%.2f", afterPrice)}"
                    )
            }
            if (this@OnSellAdapter::onItemClickListener.isInitialized) {
                holder.dataBinding.itemOnSellDataContainer.setOnClickListener {
                    onItemClickListener.onItemClick(data)
                }


                holder.dataBinding.itemOnSellDataContainer.setOnLongClickListener {
                    onItemClickListener.onItemLongClick(data)
                    true
                }
            }
        }


    }

    override fun getItemCount() = dataList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<OnSellDataList.MapData>?) {
        dataList.clear()
        if (it != null) {
            dataList.addAll(it)
        }
        notifyDataSetChanged()
    }

    fun setLoadMoreData(it: List<OnSellDataList.MapData>?) {
        if (it != null) {
            dataList.addAll(it)
            notifyItemChanged(dataList.size, it.size)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(data: BaseData)
        fun onItemLongClick(data: BaseData)
    }
}