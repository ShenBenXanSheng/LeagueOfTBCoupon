package com.example.leagueoftbcoupon.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.leagueoftbcoupon.R
import com.example.leagueoftbcoupon.databinding.ItemSelectedDataBinding
import com.example.leagueoftbcoupon.domain.BaseData
import com.example.leagueoftbcoupon.domain.dataBean.SelectedDataBean
import com.example.leagueoftbcoupon.utils.UrlUtils

class SelectedDataAdapter : RecyclerView.Adapter<SelectedDataAdapter.InnerHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener
    private val selectedList = mutableListOf<BaseData>()

    class InnerHolder(itemView: View, val dataBinding: ItemSelectedDataBinding) :
        RecyclerView.ViewHolder(itemView) {

    }

    companion object {
        @JvmStatic
        @BindingAdapter("selected_cover")
        fun setSelectedCover(imageView: ImageView, cover: Any) {
            Glide.with(imageView).load(cover).into(imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val dataBinding = DataBindingUtil.inflate<ItemSelectedDataBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_selected_data,
            parent, false
        )

        return InnerHolder(dataBinding.root, dataBinding)

    }

    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        val selectedItemData = selectedList[position]

        selectedItemData.apply {
            holder.dataBinding.apply {
                if (title.isBlank() && pictUrl.isBlank()) {
                    selectedDataContainer.visibility = View.GONE
                } else {
                    selectedDataContainer.visibility = View.VISIBLE
                }
                val cover = if (pictUrl.isNotBlank()) {
                    UrlUtils.getCoverUrl(pictUrl)
                } else {
                    R.mipmap.paimeng
                }

                var couponMsg = ""
                var beforePrice = ""
                if (zkFinalPrice != null && couponAmount != null && zkFinalPrice.isNotBlank() && couponAmount != 0) {
                    beforePrice = "原价:${zkFinalPrice}"
                    couponMsg = "领券立领${couponAmount}元"
                    selectedCouponLv.mText = couponMsg
                    selectedCouponLv.visibility = View.VISIBLE
                    selectedShopBt.visibility = View.VISIBLE
                } else {
                    beforePrice = "暂无优惠券"
                    selectedCouponLv.visibility = View.GONE
                    selectedShopBt.visibility = View.GONE
                }

                selectedData = SelectedDataBean(title, cover, beforePrice)

                selectedDataContainer.setOnLongClickListener {
                    onItemClickListener.onLongItemClickListener(selectedItemData,selectedShopBt)
                    true
                }

                selectedShopBt.setOnClickListener {
                    onItemClickListener.onItemClickListener(selectedItemData )
                }
            }
        }


    }

    override fun getItemCount() = selectedList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(it: List<BaseData>?) {
        selectedList.clear()
        if (it != null) {
            selectedList.addAll(it)
            notifyDataSetChanged()
        }
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }


    interface OnItemClickListener{
        fun onItemClickListener(data: BaseData)
        fun onLongItemClickListener(data: BaseData, selectedShopBt: Button)
    }
}